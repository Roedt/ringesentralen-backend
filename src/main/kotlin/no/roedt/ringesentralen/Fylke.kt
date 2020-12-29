package no.roedt.ringesentralen

enum class Fylke(val nr: Int) {
    Uassosiert(-1),
    Oestfold(1),
    Akershus(2),
    Oslo(3),
    Hedmark(4),
    Oppland(5),
    Buskerud(6),
    Vestfold(7),
    Telemark(8),
    AustAgder(9),
    VestAgder(10),
    Rogaland(11),
    Hordaland(12),
    SognOgFjordane(14),
    MoereOgRomsdal(15),
    Nordland(18),
    Troms(19),
    Finnmark(20),
    Svalbard(21),
    Troendelag(50);

    companion object {
        fun from(value: Int): Fylke = values().first { it.nr == value }
    }
}