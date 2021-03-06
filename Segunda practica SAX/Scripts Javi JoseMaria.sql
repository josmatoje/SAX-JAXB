
CREATE LOGIN apuestas with password='apuestas',
DEFAULT_DATABASE=APUESTAS
USE APUESTAS
CREATE USER apuestas FOR LOGIN apuestas
GRANT EXECUTE, INSERT, UPDATE,
SELECT TO apuestas


	INSERT INTO COMBATES (FECHA_INICIO, FECHA_FIN, ADMITE_APUESTAS, MAX_APUESTAS_POR_GANADOR, MAX_APUESTAS_POR_TIPO_VICTORIA, MAX_APUESTAS_POR_PUNTUACION) VALUES
	('01/5/21', '02/5/21', 1, 1000, 1000, 10000),
	('05/8/21', '06/8/21', 1, 1000, 1000, 1000)

    INSERT INTO COMPETICIONES (ID_COMBATE, NOMBRE_BOXEADOR) VALUES
    (3, 'Paco Abreu'),
    (3, 'Mac Janson'),
    (4, 'Antonio Sanjuan'),
    (4, 'Mac Janson')

	GO
	--Inserta una apuesta en la tabla apuestas y hace una insercion en la tabla tipo de apuestas
    CREATE OR ALTER PROCEDURE InstertarApuesta
        @NOMBRE_USUARIO VarChar(20),
        @ID_COMBATE Int,
        @CANTIDAD float,
        @CUOTA float,
        @GANADOR VARCHAR(50),
        @PUNTUACION SMALLINT,
        @TIPOVICTORIA VARCHAR(20)
		--A�adir OUTPUT para tratar los posibles errores
    AS
    BEGIN
	DECLARE @IDINSERTADO SMALLINT --Variable para almacenar el nuevo ID
	BEGIN TRANSACTION
        INSERT INTO APUESTAS(NOMBRE_USUARIO,ID_COMBATE,CANTIDAD,CUOTA) VALUES (@NOMBRE_USUARIO,@ID_COMBATE,@CANTIDAD,@CUOTA)
		SELECT @IDINSERTADO = MAX(ID) FROM APUESTAS --@@IDENTITY daba problemas al usarlo, no sacaba el ultimo ID generado y entraba en conflicto con las FK de las tablas de tipo de apuestas
			WHERE @NOMBRE_USUARIO=NOMBRE_USUARIO AND
					@ID_COMBATE=ID_COMBATE AND
					@CANTIDAD=CANTIDAD AND
					@CUOTA=CUOTA				--Condiciones del where necesarias en caso de no insertar apuesta correctamente es necesario
		IF (@IDINSERTADO IS NOT NULL)
		BEGIN
			IF (@TIPOVICTORIA = 'JAVI' AND @PUNTUACION =-1)
				INSERT INTO APUESTAS_POR_GANADOR(ID_APUESTA, NOMBRE_GANADOR) VALUES (@IDINSERTADO,@GANADOR)
			ELSE
				IF (@GANADOR = 'Indefinido' AND @PUNTUACION = -1)
					INSERT INTO APUESTAS_POR_TIPO_DE_VICTORIA(ID_APUESTA, TIPO_DE_VICTORIA) VALUES (@IDINSERTADO,@TIPOVICTORIA)
				ELSE
					INSERT INTO APUESTAS_POR_PUNTUACION (ID_APUESTA, PUNTUACION) VALUES (@IDINSERTADO, @PUNTUACION)
			
			COMMIT
		END
		ELSE
			ROLLBACK
    END
GO


select * from APUESTAS
select * from BOXEADORES
select * from COMBATES
select * from APUESTAS_POR_GANADOR
select * from APUESTAS_POR_PUNTUACION
select * from APUESTAS_POR_TIPO_DE_VICTORIA
select * from USUARIOS
SELECT * FROM PUNTUACIONES
SELECT * FROM COMPETICIONES

BEGIN TRANSACTION

EXECUTE InstertarApuesta 'Amador',3, 12, 3, 'Mac Janson', -1, 'JAVI'
EXECUTE InstertarApuesta 'Jesus',4, 8, 2.3, 'Mac Janson', -1, 'JAVI'
EXECUTE InstertarApuesta 'Alejandro',3, 4, 3, 'Indefinido', 7, 'JAVI'
EXECUTE InstertarApuesta 'Amador',4, 4, 38.46, 'Indefinido', 32, 'JAVI'
EXECUTE InstertarApuesta 'Alejandro',4, 6.5, 3.14, 'Indefinido', -1, 'PUNTUACION'
EXECUTE InstertarApuesta 'Jesus',3, 7, 8.2, 'Indefinido', -1, 'KO'

ROLLBACK
commit
