CREATE OR REPLACE VIEW v_personerSomKanRinges AS
select p.lokallag   AS lokallag,
       p.id         AS id,
       p.hypersysID AS hypersysID,
       p.fylke      AS fylke,
       p.groupID    AS brukergruppe,
       (select samtale.datetime
        from samtale
        where (samtale.ringt = p.id)
        order by samtale.datetime desc
        limit 1)    AS sisteSamtale
from (person p left join lokallag l on ((p.lokallag = l.id)))
where (p.groupID in (select brukergruppe.id
                     from brukergruppe
                     where ((brukergruppe.navn = 'klar til å ringes') or
                            (brukergruppe.navn = 'prioritert å ringe'))) and
       (now() - (select coalesce ((select max(samtale.datetime) from samtale where (samtale.ringt = p.id)), to_timestamp(0))) > INTERVAL '1 DAY')
    and
       exists(select 1
              from oppslag o
              where ((o.ringt = p.id) and ((now() - o.datetime) < INTERVAL '1 HOUR'))) is false);