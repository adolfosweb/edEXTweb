USE edext_db;

-- Correcciones de fecha
UPDATE InscripcionCurso SET fechaInscripcion='2024-02-18'
 WHERE edicionCurso_nombre='Taller de robótica educativa - 2024' AND estudiante_nickname='weiss';
UPDATE InscripcionCurso SET fechaInscripcion='2026-02-23'
 WHERE edicionCurso_nombre='Taller de robótica educativa - 2026' AND estudiante_nickname='roro';

-- Notas
UPDATE InscripcionCurso SET nota=6  WHERE edicionCurso_nombre='Flor del Ceibo - 2010' AND estudiante_nickname='eleven11';
UPDATE InscripcionCurso SET nota=8  WHERE edicionCurso_nombre='Flor del Ceibo - 2010' AND estudiante_nickname='chechi';
UPDATE InscripcionCurso SET nota=9  WHERE edicionCurso_nombre='Flor del Ceibo - 2012' AND estudiante_nickname='costas';
UPDATE InscripcionCurso SET nota=0  WHERE edicionCurso_nombre='Flor del Ceibo - 2012' AND estudiante_nickname='weiss';
UPDATE InscripcionCurso SET nota=9  WHERE edicionCurso_nombre='Flor del Ceibo - 2025' AND estudiante_nickname='roro';
UPDATE InscripcionCurso SET nota=6  WHERE edicionCurso_nombre='Flor del Ceibo - 2025' AND estudiante_nickname='jeffw';
UPDATE InscripcionCurso SET nota=7  WHERE edicionCurso_nombre='Dalavuelta - 2025' AND estudiante_nickname='chechi';
UPDATE InscripcionCurso SET nota=7  WHERE edicionCurso_nombre='Dalavuelta - 2025' AND estudiante_nickname='roro';
UPDATE InscripcionCurso SET nota=6  WHERE edicionCurso_nombre='Dalavuelta - 2025' AND estudiante_nickname='costas';
UPDATE InscripcionCurso SET nota=9  WHERE edicionCurso_nombre='Extensionismo Industrial - 2025' AND estudiante_nickname='costas';
UPDATE InscripcionCurso SET nota=9  WHERE edicionCurso_nombre='Extensionismo Industrial - 2025' AND estudiante_nickname='chechi';
UPDATE InscripcionCurso SET nota=1  WHERE edicionCurso_nombre='Extensionismo Industrial - 2025' AND estudiante_nickname='eleven11';
UPDATE InscripcionCurso SET nota=11 WHERE edicionCurso_nombre='Extensionismo Industrial - 2025' AND estudiante_nickname='weiss';
UPDATE InscripcionCurso SET nota=7  WHERE edicionCurso_nombre='Inclusión Energética - 2026' AND estudiante_nickname='roro';
UPDATE InscripcionCurso SET nota=11 WHERE edicionCurso_nombre='Inclusión Energética - 2026' AND estudiante_nickname='chechi';
UPDATE InscripcionCurso SET nota=0  WHERE edicionCurso_nombre='Taller de robótica educativa - 2024' AND estudiante_nickname='eleven11';
UPDATE InscripcionCurso SET nota=8  WHERE edicionCurso_nombre='Taller de robótica educativa - 2026' AND estudiante_nickname='jeffw';
UPDATE InscripcionCurso SET nota=10 WHERE edicionCurso_nombre='Taller de robótica educativa - 2026' AND estudiante_nickname='costas';
UPDATE InscripcionCurso SET nota=0  WHERE edicionCurso_nombre LIKE 'Participación en investigación%' AND estudiante_nickname IN ('chechi','weiss','roro');
UPDATE InscripcionCurso SET nota=6  WHERE edicionCurso_nombre='Talleres plenarios - 2026' AND estudiante_nickname='weiss';
UPDATE InscripcionCurso SET nota=7  WHERE edicionCurso_nombre='Talleres plenarios - 2026' AND estudiante_nickname='roro';
UPDATE InscripcionCurso SET nota=10 WHERE edicionCurso_nombre='Seminarios de Resolución de Problemas - 2026' AND estudiante_nickname='weiss';
UPDATE InscripcionCurso SET nota=2  WHERE edicionCurso_nombre='Seminarios de Resolución de Problemas - 2026' AND estudiante_nickname='roro';

-- Visitas
UPDATE Curso SET visitas = 150 WHERE nombre = 'Talleres plenarios';
UPDATE Curso SET visitas = 120 WHERE nombre = 'Seminarios de Resolución de Problemas';
UPDATE Curso SET visitas = 132 WHERE nombre = 'Dalavuelta';
UPDATE Curso SET visitas = 103 WHERE nombre = 'Extensionismo Industrial';
UPDATE Curso SET visitas = 98  WHERE nombre = 'Inclusión Energética';
UPDATE Curso SET visitas = 430 WHERE nombre = 'Flor del Ceibo';
UPDATE Curso SET visitas = 318 WHERE nombre = 'Taller de robótica educativa.';
UPDATE Curso SET visitas = 267 WHERE nombre LIKE 'Participación en investigación%';
UPDATE Curso SET visitas = 169 WHERE nombre LIKE '%Herrramientas de apoyo%';
UPDATE Curso SET visitas = 212 WHERE nombre = 'MicroBit';