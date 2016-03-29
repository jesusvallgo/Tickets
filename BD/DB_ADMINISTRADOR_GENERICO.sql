/* CATALOGOS */

/* CREATE TABLE area */
CREATE TABLE "area" (
	"id_area" int4 NOT NULL,
	"nombre" varchar(255) NOT NULL,
	PRIMARY KEY ("id_area")
);

/* FILL TABLE area */
INSERT INTO area (id_area,nombre) VALUES (1,'COORDINACIÓN ADMINISTRATIVA');
INSERT INTO area (id_area,nombre) VALUES (2,'DIRECCIÓN DE ANALISIS Y GESTION DE RIESGOS');
INSERT INTO area (id_area,nombre) VALUES (3,'DIRECCIÓN DE DIFUSION');
INSERT INTO area (id_area,nombre) VALUES (4,'DIRECCIÓN DE INSTRUMENTACION Y COMPUTO');
INSERT INTO area (id_area,nombre) VALUES (5,'DIRECCIÓN DE INVESTIGACION');
INSERT INTO area (id_area,nombre) VALUES (6,'DIRECCIÓN DE SERVICIOS TECNICOS');
INSERT INTO area (id_area,nombre) VALUES (7,'DIRECCIÓN GENERAL');
INSERT INTO area (id_area,nombre) VALUES (8,'DIRECCIÓN GENERAL ADJUNTA');
INSERT INTO area (id_area,nombre) VALUES (9,'ESCUELA NACIONAL DE PROTECCION CIVIL');

/* CREATE TABLE nivel_jerarquico */
CREATE TABLE "nivel_jerarquico" (
	"id_nivel_jerarquico" int4 NOT NULL,
	"nombre" varchar(100) NOT NULL,
	PRIMARY KEY ("id_nivel_jerarquico")
);

/* FILL TABLE nivel_jerarquico */
INSERT INTO nivel_jerarquico (id_nivel_jerarquico,nombre) VALUES (1,'DIRECTOR');
INSERT INTO nivel_jerarquico (id_nivel_jerarquico,nombre) VALUES (2,'SUBDIRECTOR');
INSERT INTO nivel_jerarquico (id_nivel_jerarquico,nombre) VALUES (3,'JEFE DE DEPARTAMENTO');
INSERT INTO nivel_jerarquico (id_nivel_jerarquico,nombre) VALUES (4,'ENLACE');
INSERT INTO nivel_jerarquico (id_nivel_jerarquico,nombre) VALUES (5,'OPERATIVO');

/* CREATE TABLE nivel_salarial */
CREATE TABLE "nivel_salarial" (
	"id_nivel_salarial" int4 NOT NULL,
	"nombre" varchar(10),
	PRIMARY KEY ("id_nivel_salarial")
);

/* FILL TABLE nivel_salarial */
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (1,'6');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (2,'7');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (3,'8');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (4,'11');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (5,'KA3');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (6,'LA1');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (7,'MA1');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (8,'MA3');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (9,'MB1');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (10,'NA1');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (11,'NA2');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (12,'NA3');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (13,'NB2');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (14,'NB3');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (15,'NC1');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (16,'NC2');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (17,'OA1');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (18,'OA3');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (19,'OB1');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (20,'OB2');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (21,'OB3');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (22,'OC1');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (23,'OC2');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (24,'OC3');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (25,'PA1');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (26,'PA2');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (27,'PA3');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (28,'PB2');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (29,'PC1');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (30,'PC2');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (31,'PC3');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (32,'PQ1');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (33,'PQ2');
INSERT INTO nivel_salarial (id_nivel_salarial,nombre) VALUES (34,'PQ3');

/* CREATE TABLE perfil */
CREATE TABLE "perfil" (
	"id_perfil" int4 NOT NULL,
	"nombre" varchar(100) NOT NULL,
	PRIMARY KEY ("id_perfil")
);

/* FILL TABLE perfil */
INSERT INTO perfil (id_perfil,nombre) VALUES (1,'Administrador');
INSERT INTO perfil (id_perfil,nombre) VALUES (2,'Usuario estándar');

/* CREATE TABLE genero */
CREATE TABLE genero(
    id_genero INTEGER PRIMARY KEY NOT NULL,
	nombre_o_a VARCHAR(1) NOT NULL,
    nombre_m_f VARCHAR(10) NOT NULL,
	nombre_h_m VARCHAR(6) NOT NULL,
    articulo_el_la VARCHAR(2) NOT NULL,
	articulo_los_las VARCHAR(3) NOT NULL
);

/* FILL TABLE genero */
INSERT INTO genero(id_genero,nombre_o_a,nombre_m_f,nombre_h_m,articulo_el_la,articulo_los_las) VALUES (1,'o','masculino','hombre','el','los');
INSERT INTO genero(id_genero,nombre_o_a,nombre_m_f,nombre_h_m,articulo_el_la,articulo_los_las) VALUES (2,'a','femenino','mujer','la','las');

/* CREATE TABLE accion */
CREATE TABLE accion(
	id_accion INTEGER PRIMARY KEY NOT NULL,
    nombre VARCHAR(15) NOT NULL
);

/* FILL TABLE accion */
INSERT INTO accion(id_accion,nombre) VALUES (1,'LOGIN');
INSERT INTO accion(id_accion,nombre) VALUES (2,'LOGOUT');
INSERT INTO accion(id_accion,nombre) VALUES (3,'CONSULTA');
INSERT INTO accion(id_accion,nombre) VALUES (4,'ALTA');
INSERT INTO accion(id_accion,nombre) VALUES (5,'CAMBIO');
INSERT INTO accion(id_accion,nombre) VALUES (6,'BAJA');

/* CREATE TABLE evento_interfaz */
CREATE TABLE evento_interfaz(
	id_evento_interfaz INTEGER PRIMARY KEY NOT NULL,
	id_accion INTEGER NOT NULL,
	nombre VARCHAR(255) NOT NULL,
	CONSTRAINT fk_evento_interfaz_accion FOREIGN KEY (id_accion) REFERENCES accion (id_accion) ON UPDATE CASCADE ON DELETE RESTRICT
);

/* FILL TABLE evento_interfaz */
INSERT INTO evento_interfaz(id_evento_interfaz,id_accion,nombre) VALUES (1,1,'Inicia sesión: Usuario con perfil de administrador');
INSERT INTO evento_interfaz(id_evento_interfaz,id_accion,nombre) VALUES (2,1,'Inicia sesión: Usuario con perfil de usuario estándar');

/* CREATE TABLE usuario */
CREATE TABLE usuario(
    username VARCHAR(10) PRIMARY KEY NOT NULL,
	password VARCHAR(50) NOT NULL,
	no_empleado int4 NOT NULL,
	rfc varchar(13) NOT NULL,
	curp varchar(18) NOT NULL,
    nombre_pila VARCHAR(50) NOT NULL,
    apellido_paterno VARCHAR(50) NOT NULL,
    apellido_materno VARCHAR(50),
	email VARCHAR(100) NOT NULL,
	puesto VARCHAR(255) NOT NULL,
    id_genero integer NOT NULL,
	id_nivel_jerarquico int4 NOT NULL,
	id_nivel_salarial int4 NOT NULL,
	id_area int4 NOT NULL,
	id_perfil INTEGER NOT NULL,
	activo BOOLEAN,
    CONSTRAINT fk_usuario_genero FOREIGN KEY (id_genero) REFERENCES genero (id_genero) ON UPDATE CASCADE ON DELETE RESTRICT,
	CONSTRAINT fk_usuario_nivel_jerarquico FOREIGN KEY (id_nivel_jerarquico) REFERENCES nivel_jerarquico (id_nivel_jerarquico) ON UPDATE CASCADE ON DELETE RESTRICT,
	CONSTRAINT fk_usuario_nivel_salarial FOREIGN KEY (id_nivel_salarial) REFERENCES nivel_salarial (id_nivel_salarial) ON UPDATE CASCADE ON DELETE RESTRICT,
	CONSTRAINT fk_usuario_area FOREIGN KEY (id_area) REFERENCES area (id_area) ON UPDATE CASCADE ON DELETE RESTRICT,
	CONSTRAINT fk_usuario_perfil FOREIGN KEY (id_perfil) REFERENCES perfil (id_perfil) ON UPDATE CASCADE ON DELETE RESTRICT
);

/* FILL TABLE usuario */
INSERT INTO usuario(username,password,no_empleado,rfc,curp,nombre_pila,apellido_paterno,apellido_materno,email,puesto,id_genero,id_nivel_jerarquico,id_nivel_salarial,id_area,id_perfil,activo) VALUES ('jvallejog','12345',626612,'VAGJ880725IT8','VAGJ880725HDFLNS04','JESÚS','VALLEJO','GONZÁLEZ','jvallejog@cenapred.unam.mx','TÉCNICO EN REDES Y ENLACES DÍGITALES',1,4,25,4,1,true);
INSERT INTO usuario(username,password,no_empleado,rfc,curp,nombre_pila,apellido_paterno,apellido_materno,email,puesto,id_genero,id_nivel_jerarquico,id_nivel_salarial,id_area,id_perfil,activo) VALUES ('ggonzaleze','12345',618715,'GOEG7406232H8','GOEG740623HDFNSB09','GABRIEL','GONZÁLEZ','ESQUIVEL','ggonzaleze@cenapred.unam.mx','JEFE DE DEPARTAMENTO DE SEGURIDAD Y ENLACES DIGITALES',1,3,17,4,2,true);
/*
INSERT INTO usuario(username,password,no_empleado,rfc,curp,nombre_pila,apellido_paterno,apellido_materno,email,puesto,id_genero,id_nivel_jerarquico,id_nivel_salarial,id_area,id_perfil,activo) VALUES ('jvallejog',encode('12345', 'base64'),626612,'VAGJ880725IT8','VAGJ880725HDFLNS04','JESÚS','VALLEJO','GONZÁLEZ','jvallejog@cenapred.unam.mx','TÉCNICO EN REDES Y ENLACES DÍGITALES',1,4,25,4,1,true);
INSERT INTO usuario(username,password,no_empleado,rfc,curp,nombre_pila,apellido_paterno,apellido_materno,email,puesto,id_genero,id_nivel_jerarquico,id_nivel_salarial,id_area,id_perfil,activo) VALUES ('ggonzaleze',encode('12345', 'base64'),618715,'GOEG7406232H8','GOEG740623HDFNSB09','GABRIEL','GONZÁLEZ','ESQUIVEL','ggonzaleze@cenapred.unam.mx','JEFE DE DEPARTAMENTO DE SEGURIDAD Y ENLACES DIGITALES',1,3,17,4,2,true);
*/

CREATE TABLE usuario_token(
	username VARCHAR(10) NOT NULL,
	token VARCHAR(255) NOT NULL,
	CONSTRAINT fk_usuario_token_usuario FOREIGN KEY (username) REFERENCES usuario (username) ON UPDATE CASCADE ON DELETE RESTRICT
);

/* CREATE TABLE bitacora */
CREATE TABLE bitacora(
	id_bitacora SERIAL PRIMARY KEY NOT NULL,
	username VARCHAR(10) NOT NULL,
	id_evento_interfaz INTEGER NOT NULL,
	fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT fk_bitacora_usuario FOREIGN KEY (username) REFERENCES usuario (username) ON UPDATE CASCADE ON DELETE RESTRICT,
	CONSTRAINT fk_bitacora_evento_interfaz FOREIGN KEY (id_evento_interfaz) REFERENCES evento_interfaz (id_evento_interfaz) ON UPDATE CASCADE ON DELETE RESTRICT
);

/* CREATE TABLE bitacora_movimiento */
CREATE TABLE bitacora_movimiento(
	id_bitacora INTEGER PRIMARY KEY NOT NULL,
	sql TEXT NOT NULL,
	CONSTRAINT fk_bitacora_movimiento_bitacora FOREIGN KEY (id_bitacora) REFERENCES bitacora (id_bitacora) ON UPDATE CASCADE ON DELETE RESTRICT
);