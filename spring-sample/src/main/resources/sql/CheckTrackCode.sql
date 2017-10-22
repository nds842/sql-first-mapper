SELECT
    track_code.id           id__l,
    track_code.track_code   codestring__s,
    track_code.postage_id   postage_id__l
FROM track_code
WHERE track_code.postage_id = :postage_id__l
