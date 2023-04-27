INSERT INTO domains(id, name)
VALUES
        (11, 'github.com'),
        (12, 'stackoverflow.com')
ON CONFLICT (id) DO NOTHING
;