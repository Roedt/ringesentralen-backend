package no.roedt.ringesentralen.verving

import io.quarkus.narayana.jta.runtime.TransactionConfiguration
import no.roedt.ringesentralen.Roles
import no.roedt.ringesentralen.hypersys.ModelConverter
import no.roedt.ringesentralen.person.GroupID
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import org.apache.poi.poifs.crypt.Decryptor
import org.apache.poi.poifs.crypt.EncryptionInfo
import org.apache.poi.poifs.filesystem.POIFSFileSystem
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import java.io.File
import javax.annotation.security.RolesAllowed
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Context
import javax.ws.rs.core.SecurityContext

@RequestScoped
@Path("/importer")
@Tag(name ="importer")
@SecurityRequirement(name = "jwt")
class NumreImporter(
    private val modelConverter: ModelConverter,
    private val personRepository: PersonRepository
) {

    @Inject
    lateinit var jwt: JsonWebToken

    @POST
    @Path("/numre")
    @Transactional
    @TransactionConfiguration(timeout = 3600)
    @RolesAllowed(Roles.admin)
    @Operation(summary = "Importer fleire numre frå fil. Dette endepunktet er i utgangspunktet kun tenkt for lokal bruk.", description = Roles.admin)
    fun import(@Context ctx: SecurityContext, @QueryParam("fileLocation") fileLocation: String, @QueryParam("passord") passord: String) =
        readFile(fileLocation, passord)
        .getSheetAt(0)
        .mapNotNull { toPerson(it) }
        .forEach { personRepository.persist(it) }

    private fun readFile(fileLocation: String, passord: String): Workbook {
        val fileSystem = POIFSFileSystem(File(fileLocation), true)
        val decryptor = Decryptor.getInstance(EncryptionInfo(fileSystem))
        if (!decryptor.verifyPassword(passord)) {
            throw RuntimeException("Unable to process: document is encrypted.")
        }
        return XSSFWorkbook(decryptor.getDataStream(fileSystem))
    }

    private fun toPerson(it: Row): Person? {
        if (it.getCell(0).stringCellValue == "IperID") return null
        val postnummer = it.getCell(13).stringCellValue.toInt()
        var fylke = it.getCell(1)?.stringCellValue?.toInt() ?: modelConverter.toFylke(postnummer = postnummer)
        if (fylke == 16 || fylke == 17) fylke = 50

        var fornavn = it.getCell(5).stringCellValue
        if (it.getCell(6) != null) fornavn = fornavn + " " + it.getCell(6).stringCellValue
        val etternavn = it.getCell(7).stringCellValue
        val telefonnummer = "+47${it.getCell(18).stringCellValue}"

        return Person(
            hypersysID = null,
            fornavn = fornavn,
            etternavn = etternavn,
            telefonnummer = telefonnummer,
            email = null,
            postnummer = postnummer,
            fylke = fylke,
            groupID = GroupID.KlarTilAaRinges.nr,
            lokallag = modelConverter.toLokallag(postnummer)
        )
    }
}