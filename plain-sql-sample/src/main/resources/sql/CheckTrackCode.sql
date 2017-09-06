SELECT
    trackcode.id           id__l
    trackcode.code         codestring__s,
    trackcode.parcelid     parcelid__l,
    trackcode.dateassign   dateassign_d
FROM trackcode
WHERE parcelid = :parcelid__l
