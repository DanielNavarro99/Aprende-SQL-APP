package com.sqlmimo.model

object CourseData {

    // ── Tablas reutilizables ──────────────────────────────────
    private val tblEmpleados = TableData(
        name = "empleados",
        columns = listOf("id", "nombre", "depto", "salario"),
        rows = listOf(
            listOf("1", "Ana López", "Ventas", "25000"),
            listOf("2", "Carlos Ruiz", "IT", "35000"),
            listOf("3", "María García", "RRHH", "22000"),
            listOf("4", "Luis Torres", "IT", "38000"),
            listOf("5", "Pedro Díaz", "IT", "32000"),
            listOf("6", "Sofía Ramos", "Ventas", "28000")
        )
    )
    private val tblProductos = TableData(
        name = "productos",
        columns = listOf("id", "nombre", "precio", "stock"),
        rows = listOf(
            listOf("1", "Laptop", "15000", "5"),
            listOf("2", "Mouse", "350", "20"),
            listOf("3", "Teclado", "800", "15"),
            listOf("4", "Monitor", "8500", "3"),
            listOf("5", "Webcam", "1200", "8"),
            listOf("6", "Auriculares", "650", "12")
        )
    )
    private val tblClientes = TableData(
        name = "clientes",
        columns = listOf("id", "nombre", "ciudad", "edad"),
        rows = listOf(
            listOf("1", "Ana García", "CDMX", "28"),
            listOf("2", "Alberto Ruiz", "GDL", "35"),
            listOf("3", "Carmen López", "MTY", "42"),
            listOf("4", "Andrés Torres", "CDMX", "31"),
            listOf("5", "Beatriz Díaz", "GDL", "27")
        )
    )
    private val tblPedidos = TableData(
        name = "pedidos",
        columns = listOf("id", "cliente_id", "total"),
        rows = listOf(
            listOf("1", "1", "500"),
            listOf("2", "3", "1200"),
            listOf("3", "1", "350"),
            listOf("4", "2", "800"),
            listOf("5", "3", "2500")
        )
    )

    // ── Módulos ───────────────────────────────────────────────
    val modules: List<Module> = listOf(

        // ── M1: SELECT básico ─────────────────────────────────
        Module(
            id = "m1", icon = "📋",
            title = "SELECT — Lo básico",
            description = "Tu primera consulta SQL",
            difficulty = Difficulty.BEGINNER,
            lessons = listOf(
                Lesson("t1", "¿Qué es SQL?", LessonType.THEORY, 5,
                    theory = TheoryContent(
                        text = "SQL es el lenguaje estándar para gestionar bases de datos relacionales. Con él puedes consultar, insertar, actualizar y eliminar datos de tablas.",
                        code = "-- Tu primera consulta\nSELECT * FROM empleados;\n\n-- Columnas específicas\nSELECT nombre, salario FROM empleados;",
                        note = "Las bases de datos guardan información en tablas, como hojas de cálculo pero más poderosas."
                    )
                ),
                Lesson("l1", "SELECT * FROM", LessonType.EXERCISE, 10,
                    exercise = ExerciseContent(
                        task = "La tabla productos está lista. Escribe la consulta para traer TODOS sus registros.",
                        hint = "SELECT * FROM nombre_tabla — el asterisco (*) significa todas las columnas.",
                        tables = listOf(tblProductos),
                        answer = "select * from productos",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("select") && s.contains("from") && s.contains("productos")
                        }
                    )
                ),
                Lesson("l2", "Columnas específicas", LessonType.EXERCISE, 15,
                    exercise = ExerciseContent(
                        task = "De la tabla empleados, trae solo las columnas nombre y salario.",
                        hint = "Escribe los nombres separados por coma: SELECT col1, col2 FROM tabla",
                        tables = listOf(tblEmpleados),
                        answer = "select nombre, salario from empleados",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("select") && s.contains("nombre") && s.contains("salario") && s.contains("from empleados")
                        }
                    )
                ),
                Lesson("l3", "Alias con AS", LessonType.EXERCISE, 15,
                    exercise = ExerciseContent(
                        task = "De productos, trae nombre con alias 'Producto' y precio con alias 'Costo'.",
                        hint = "SELECT nombre AS 'Producto', precio AS 'Costo' FROM tabla",
                        tables = listOf(tblProductos),
                        answer = "select nombre as 'producto', precio as 'costo' from productos",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("nombre") && s.contains("as") && s.contains("precio") && s.contains("productos")
                        }
                    )
                ),
                Lesson("l4", "DISTINCT", LessonType.EXERCISE, 15,
                    exercise = ExerciseContent(
                        task = "De empleados, trae los valores únicos de la columna depto (sin repetidos).",
                        hint = "SELECT DISTINCT columna FROM tabla",
                        tables = listOf(tblEmpleados),
                        answer = "select distinct depto from empleados",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("distinct") && s.contains("depto") && s.contains("empleados")
                        }
                    )
                )
            )
        ),

        // ── M2: WHERE ─────────────────────────────────────────
        Module(
            id = "m2", icon = "🔍",
            title = "WHERE — Filtros",
            description = "Encuentra exactamente lo que buscas",
            difficulty = Difficulty.BEGINNER,
            lessons = listOf(
                Lesson("t2", "La cláusula WHERE", LessonType.THEORY, 5,
                    theory = TheoryContent(
                        text = "WHERE filtra las filas que cumplen una condición. Solo se devuelven las filas donde la condición es verdadera.",
                        code = "SELECT * FROM empleados\nWHERE depto = 'IT';\n\nSELECT * FROM productos\nWHERE precio > 1000;",
                        note = "Operadores: = (igual), != (diferente), > (mayor), < (menor), >= (mayor o igual), <= (menor o igual)."
                    )
                ),
                Lesson("l5", "WHERE con texto", LessonType.EXERCISE, 15,
                    exercise = ExerciseContent(
                        task = "De empleados, trae todos los que trabajan en el departamento IT.",
                        hint = "WHERE depto = 'IT' — los textos van entre comillas simples.",
                        tables = listOf(tblEmpleados),
                        answer = "select * from empleados where depto = 'it'",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("from empleados") && s.contains("where") && s.contains("depto") && s.contains("it")
                        }
                    )
                ),
                Lesson("l6", "WHERE con números", LessonType.EXERCISE, 15,
                    exercise = ExerciseContent(
                        task = "De productos, trae los productos con stock menor a 10.",
                        hint = "Usa el operador < para menor que. Los números no llevan comillas.",
                        tables = listOf(tblProductos),
                        answer = "select * from productos where stock < 10",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("productos") && s.contains("where") && s.contains("stock") && s.contains("< 10")
                        }
                    )
                ),
                Lesson("l7", "AND — dos condiciones", LessonType.EXERCISE, 20,
                    exercise = ExerciseContent(
                        task = "De empleados, trae los que son del depto IT Y tienen salario mayor o igual a 35000.",
                        hint = "WHERE depto = 'IT' AND salario >= 35000",
                        tables = listOf(tblEmpleados),
                        answer = "select * from empleados where depto = 'it' and salario >= 35000",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("empleados") && s.contains("where") && s.contains("and") && s.contains("it") && (s.contains(">= 35000") || s.contains(">=35000"))
                        }
                    )
                ),
                Lesson("l8", "OR — una u otra condición", LessonType.EXERCISE, 20,
                    exercise = ExerciseContent(
                        task = "De empleados, trae los que son de Ventas O de RRHH.",
                        hint = "WHERE depto = 'Ventas' OR depto = 'RRHH'",
                        tables = listOf(tblEmpleados),
                        answer = "select * from empleados where depto = 'ventas' or depto = 'rrhh'",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("empleados") && s.contains("where") && s.contains("or") && s.contains("ventas") && s.contains("rrhh")
                        }
                    )
                ),
                Lesson("l9", "BETWEEN — rango", LessonType.EXERCISE, 20,
                    exercise = ExerciseContent(
                        task = "De productos, trae los que tienen precio entre 500 y 5000.",
                        hint = "WHERE precio BETWEEN 500 AND 5000",
                        tables = listOf(tblProductos),
                        answer = "select * from productos where precio between 500 and 5000",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("productos") && s.contains("between") && s.contains("500") && s.contains("5000")
                        }
                    )
                ),
                Lesson("l10", "LIKE — búsqueda de texto", LessonType.EXERCISE, 20,
                    exercise = ExerciseContent(
                        task = "De clientes, trae todos cuyo nombre empiece con la letra A.",
                        hint = "WHERE nombre LIKE 'A%' — el % es comodín",
                        tables = listOf(tblClientes),
                        answer = "select * from clientes where nombre like 'a%'",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("clientes") && s.contains("like") && s.contains("a%")
                        }
                    )
                ),
                Lesson("l11", "IN — lista de valores", LessonType.EXERCISE, 20,
                    exercise = ExerciseContent(
                        task = "De empleados, trae los de IT, Ventas y RRHH usando IN.",
                        hint = "WHERE depto IN ('IT','Ventas','RRHH')",
                        tables = listOf(tblEmpleados),
                        answer = "select * from empleados where depto in ('it','ventas','rrhh')",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("empleados") && s.contains("in") && s.contains("it") && s.contains("ventas") && s.contains("rrhh")
                        }
                    )
                )
            )
        ),

        // ── M3: ORDER BY y LIMIT ──────────────────────────────
        Module(
            id = "m3", icon = "📊",
            title = "ORDER BY y LIMIT",
            description = "Ordena y pagina tus resultados",
            difficulty = Difficulty.BEGINNER,
            lessons = listOf(
                Lesson("t3", "Ordenar y limitar", LessonType.THEORY, 5,
                    theory = TheoryContent(
                        text = "ORDER BY ordena los resultados. ASC es de menor a mayor (por defecto), DESC de mayor a menor. LIMIT restringe cuántas filas se devuelven.",
                        code = "SELECT * FROM productos\nORDER BY precio DESC\nLIMIT 5;\n-- Los 5 productos más caros",
                        note = "ORDER BY puede usar múltiples columnas: ORDER BY depto ASC, salario DESC"
                    )
                ),
                Lesson("l12", "ORDER BY ASC", LessonType.EXERCISE, 15,
                    exercise = ExerciseContent(
                        task = "De empleados, trae todos ordenados por salario de menor a mayor.",
                        hint = "ORDER BY salario ASC (o solo ORDER BY salario)",
                        tables = listOf(tblEmpleados),
                        answer = "select * from empleados order by salario",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("empleados") && s.contains("order by") && s.contains("salario")
                        }
                    )
                ),
                Lesson("l13", "ORDER BY DESC", LessonType.EXERCISE, 15,
                    exercise = ExerciseContent(
                        task = "De productos, trae todos ordenados por precio de mayor a menor.",
                        hint = "ORDER BY precio DESC",
                        tables = listOf(tblProductos),
                        answer = "select * from productos order by precio desc",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("productos") && s.contains("order by") && s.contains("precio") && s.contains("desc")
                        }
                    )
                ),
                Lesson("l14", "LIMIT", LessonType.EXERCISE, 15,
                    exercise = ExerciseContent(
                        task = "De empleados, trae los 3 con mayor salario.",
                        hint = "ORDER BY salario DESC LIMIT 3",
                        tables = listOf(tblEmpleados),
                        answer = "select * from empleados order by salario desc limit 3",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("empleados") && s.contains("order by") && s.contains("desc") && s.contains("limit 3")
                        }
                    )
                )
            )
        ),

        // ── M4: Funciones agregadas ───────────────────────────
        Module(
            id = "m4", icon = "🧮",
            title = "Funciones agregadas",
            description = "COUNT, SUM, AVG, MAX, MIN",
            difficulty = Difficulty.INTERMEDIATE,
            lessons = listOf(
                Lesson("t4", "Funciones de resumen", LessonType.THEORY, 5,
                    theory = TheoryContent(
                        text = "Las funciones agregadas calculan un valor a partir de múltiples filas. Son esenciales para reportes y análisis.",
                        code = "SELECT COUNT(*) FROM pedidos;   -- contar\nSELECT SUM(total) FROM pedidos;  -- sumar\nSELECT AVG(precio) FROM prods;   -- promedio\nSELECT MAX(salario) FROM emps;   -- máximo\nSELECT MIN(precio) FROM prods;   -- mínimo",
                        note = "NULL es ignorado por todas las funciones excepto COUNT(*)."
                    )
                ),
                Lesson("l15", "COUNT(*)", LessonType.EXERCISE, 15,
                    exercise = ExerciseContent(
                        task = "¿Cuántos registros tiene la tabla clientes?",
                        hint = "SELECT COUNT(*) FROM tabla",
                        tables = listOf(tblClientes),
                        answer = "select count(*) from clientes",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("count(*)") && s.contains("clientes")
                        }
                    )
                ),
                Lesson("l16", "SUM y AVG", LessonType.EXERCISE, 20,
                    exercise = ExerciseContent(
                        task = "De empleados, calcula la suma total y el promedio de salarios en una sola consulta.",
                        hint = "SELECT SUM(columna), AVG(columna) FROM tabla",
                        tables = listOf(tblEmpleados),
                        answer = "select sum(salario), avg(salario) from empleados",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("sum(salario)") && s.contains("avg(salario)") && s.contains("empleados")
                        }
                    )
                ),
                Lesson("l17", "MAX y MIN", LessonType.EXERCISE, 20,
                    exercise = ExerciseContent(
                        task = "De productos, trae el precio más alto y el precio más bajo.",
                        hint = "SELECT MAX(precio), MIN(precio) FROM productos",
                        tables = listOf(tblProductos),
                        answer = "select max(precio), min(precio) from productos",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("max(precio)") && s.contains("min(precio)") && s.contains("productos")
                        }
                    )
                )
            )
        ),

        // ── M5: GROUP BY y HAVING ─────────────────────────────
        Module(
            id = "m5", icon = "📦",
            title = "GROUP BY y HAVING",
            description = "Agrupa y filtra grupos de datos",
            difficulty = Difficulty.INTERMEDIATE,
            lessons = listOf(
                Lesson("t5", "Agrupar datos", LessonType.THEORY, 5,
                    theory = TheoryContent(
                        text = "GROUP BY agrupa filas con el mismo valor en una columna. Se usa con funciones agregadas para obtener resúmenes por categoría.",
                        code = "SELECT depto, COUNT(*), AVG(salario)\nFROM empleados\nGROUP BY depto;\n\nSELECT depto, COUNT(*) AS total\nFROM empleados\nGROUP BY depto\nHAVING COUNT(*) > 2;",
                        note = "WHERE filtra filas ANTES de agrupar. HAVING filtra grupos DESPUÉS de agrupar."
                    )
                ),
                Lesson("l18", "GROUP BY básico", LessonType.EXERCISE, 20,
                    exercise = ExerciseContent(
                        task = "De empleados, cuenta cuántos empleados hay en cada departamento.",
                        hint = "SELECT depto, COUNT(*) FROM empleados GROUP BY depto",
                        tables = listOf(tblEmpleados),
                        answer = "select depto, count(*) from empleados group by depto",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("count(*)") && s.contains("group by") && s.contains("empleados")
                        }
                    )
                ),
                Lesson("l19", "GROUP BY con AVG", LessonType.EXERCISE, 25,
                    exercise = ExerciseContent(
                        task = "De empleados, calcula el salario promedio por departamento.",
                        hint = "SELECT depto, AVG(salario) FROM empleados GROUP BY depto",
                        tables = listOf(tblEmpleados),
                        answer = "select depto, avg(salario) from empleados group by depto",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("avg(salario)") && s.contains("group by") && s.contains("empleados")
                        }
                    )
                ),
                Lesson("l20", "HAVING", LessonType.EXERCISE, 25,
                    exercise = ExerciseContent(
                        task = "Muestra solo los departamentos con más de 2 empleados.",
                        hint = "GROUP BY depto HAVING COUNT(*) > 2",
                        tables = listOf(tblEmpleados),
                        answer = "select depto, count(*) from empleados group by depto having count(*) > 2",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("group by") && s.contains("having") && s.contains("count(*)") && (s.contains("> 2") || s.contains(">2"))
                        }
                    )
                )
            )
        ),

        // ── M6: JOINs ─────────────────────────────────────────
        Module(
            id = "m6", icon = "🔗",
            title = "JOIN — Unir tablas",
            description = "Combina información de múltiples tablas",
            difficulty = Difficulty.INTERMEDIATE,
            lessons = listOf(
                Lesson("t6", "¿Qué es un JOIN?", LessonType.THEORY, 5,
                    theory = TheoryContent(
                        text = "JOIN combina filas de dos o más tablas basándose en una columna relacionada. Es uno de los conceptos más poderosos de SQL.",
                        code = "-- INNER JOIN: solo coincidencias\nSELECT p.id, c.nombre, p.total\nFROM pedidos p\nINNER JOIN clientes c ON p.cliente_id = c.id;\n\n-- LEFT JOIN: todos los de la izquierda\nSELECT c.nombre, COUNT(p.id)\nFROM clientes c\nLEFT JOIN pedidos p ON c.id = p.cliente_id\nGROUP BY c.nombre;",
                        note = "Tipos: INNER JOIN, LEFT JOIN, RIGHT JOIN, FULL JOIN."
                    )
                ),
                Lesson("l21", "INNER JOIN", LessonType.EXERCISE, 30,
                    exercise = ExerciseContent(
                        task = "Une pedidos con clientes para ver el nombre del cliente en cada pedido.",
                        hint = "FROM pedidos INNER JOIN clientes ON pedidos.cliente_id = clientes.id",
                        tables = listOf(tblPedidos, tblClientes.copy(
                            rows = listOf(
                                listOf("1","Ana López","CDMX","28"),
                                listOf("2","Carlos Ruiz","GDL","35"),
                                listOf("3","María García","MTY","42")
                            )
                        )),
                        answer = "select pedidos.id, clientes.nombre, pedidos.total from pedidos inner join clientes on pedidos.cliente_id = clientes.id",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("inner join") && s.contains("clientes") && s.contains("pedidos") && s.contains("on") && s.contains("cliente_id")
                        }
                    )
                ),
                Lesson("l22", "LEFT JOIN", LessonType.EXERCISE, 30,
                    exercise = ExerciseContent(
                        task = "Muestra TODOS los clientes aunque no tengan pedidos. Usa LEFT JOIN.",
                        hint = "FROM clientes LEFT JOIN pedidos ON clientes.id = pedidos.cliente_id",
                        tables = listOf(tblClientes, tblPedidos),
                        answer = "select clientes.nombre, pedidos.id, pedidos.total from clientes left join pedidos on clientes.id = pedidos.cliente_id",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("left join") && s.contains("clientes") && s.contains("pedidos") && s.contains("on")
                        }
                    )
                )
            )
        ),

        // ── M7: DML ───────────────────────────────────────────
        Module(
            id = "m7", icon = "✏️",
            title = "INSERT, UPDATE, DELETE",
            description = "Modifica los datos de tus tablas",
            difficulty = Difficulty.INTERMEDIATE,
            lessons = listOf(
                Lesson("t7", "Modificar datos", LessonType.THEORY, 5,
                    theory = TheoryContent(
                        text = "DML (Data Manipulation Language) incluye los comandos para insertar, actualizar y eliminar filas en una tabla.",
                        code = "INSERT INTO productos (nombre, precio, stock)\nVALUES ('Tablet', 7500, 10);\n\nUPDATE productos\nSET precio = 8000\nWHERE id = 1;\n\nDELETE FROM productos\nWHERE id = 5;",
                        note = "¡SIEMPRE usa WHERE en UPDATE y DELETE para no afectar toda la tabla!"
                    )
                ),
                Lesson("l23", "INSERT INTO", LessonType.EXERCISE, 20,
                    exercise = ExerciseContent(
                        task = "Inserta un nuevo producto: nombre='Tablet', precio=7500, stock=10.",
                        hint = "INSERT INTO productos (nombre, precio, stock) VALUES ('Tablet', 7500, 10)",
                        tables = listOf(tblProductos),
                        answer = "insert into productos (nombre, precio, stock) values ('tablet', 7500, 10)",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("insert into productos") && s.contains("values") && s.contains("tablet") && s.contains("7500")
                        }
                    )
                ),
                Lesson("l24", "UPDATE con WHERE", LessonType.EXERCISE, 20,
                    exercise = ExerciseContent(
                        task = "Actualiza el precio del producto con id=2 a 400.",
                        hint = "UPDATE productos SET precio = 400 WHERE id = 2",
                        tables = listOf(tblProductos),
                        answer = "update productos set precio = 400 where id = 2",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("update productos") && s.contains("set") && s.contains("precio") && s.contains("400") && s.contains("where") && s.contains("id")
                        }
                    )
                ),
                Lesson("l25", "DELETE con WHERE", LessonType.EXERCISE, 20,
                    exercise = ExerciseContent(
                        task = "Elimina de empleados al empleado con id=3.",
                        hint = "DELETE FROM empleados WHERE id = 3",
                        tables = listOf(tblEmpleados),
                        answer = "delete from empleados where id = 3",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("delete from empleados") && s.contains("where") && (s.contains("id = 3") || s.contains("id=3"))
                        }
                    )
                )
            )
        ),

        // ── M8: Avanzado ──────────────────────────────────────
        Module(
            id = "m8", icon = "🚀",
            title = "Consultas avanzadas",
            description = "Subconsultas, CASE y desafíos",
            difficulty = Difficulty.ADVANCED,
            lessons = listOf(
                Lesson("t8", "Técnicas avanzadas", LessonType.THEORY, 5,
                    theory = TheoryContent(
                        text = "Las subconsultas permiten usar el resultado de una consulta dentro de otra. CASE funciona como un if/else en SQL.",
                        code = "-- Subconsulta en WHERE\nSELECT * FROM empleados\nWHERE salario > (SELECT AVG(salario) FROM empleados);\n\n-- CASE\nSELECT nombre,\n  CASE\n    WHEN salario >= 35000 THEN 'Senior'\n    WHEN salario >= 25000 THEN 'Mid'\n    ELSE 'Junior'\n  END AS nivel\nFROM empleados;",
                        note = "Las subconsultas pueden ir en SELECT, FROM o WHERE. Se ejecutan de adentro hacia afuera."
                    )
                ),
                Lesson("l26", "Subconsulta en WHERE", LessonType.EXERCISE, 35,
                    exercise = ExerciseContent(
                        task = "De empleados, trae los que tienen salario mayor al promedio de todos.",
                        hint = "WHERE salario > (SELECT AVG(salario) FROM empleados)",
                        tables = listOf(tblEmpleados),
                        answer = "select * from empleados where salario > (select avg(salario) from empleados)",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("where") && s.contains("select avg(salario)") && s.contains("from empleados")
                        }
                    )
                ),
                Lesson("l27", "CASE — columna calculada", LessonType.EXERCISE, 35,
                    exercise = ExerciseContent(
                        task = "Agrega columna 'nivel': salario>=35000 → 'Senior', >=25000 → 'Mid', else → 'Junior'.",
                        hint = "CASE WHEN salario >= 35000 THEN 'Senior' WHEN salario >= 25000 THEN 'Mid' ELSE 'Junior' END AS nivel",
                        tables = listOf(tblEmpleados),
                        answer = "select nombre, salario, case when salario >= 35000 then 'senior' when salario >= 25000 then 'mid' else 'junior' end as nivel from empleados",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("case") && s.contains("when") && s.contains("then") && s.contains("else") && s.contains("end") && s.contains("empleados")
                        }
                    )
                ),
                Lesson("l28", "🏆 Desafío final", LessonType.EXERCISE, 50,
                    exercise = ExerciseContent(
                        task = "Muestra el depto, cantidad de empleados y salario promedio, SOLO para deptos con promedio > 25000. Ordena por promedio DESC.",
                        hint = "SELECT depto, COUNT(*), AVG(salario) FROM empleados GROUP BY depto HAVING AVG(salario) > 25000 ORDER BY AVG(salario) DESC",
                        tables = listOf(tblEmpleados),
                        answer = "select depto, count(*), avg(salario) from empleados group by depto having avg(salario) > 25000 order by avg(salario) desc",
                        checkFn = { u ->
                            val s = normalizeSQL(u)
                            s.contains("group by") && s.contains("having") && s.contains("25000") && s.contains("order by") && s.contains("desc")
                        }
                    )
                )
            )
        )
    )

    // ── Preguntas del examen ──────────────────────────────────
    val examQuestions: List<ExamQuestion> = listOf(
        ExamQuestion("¿Cuál es la sintaxis para traer todas las columnas de una tabla?",
            listOf("GET ALL FROM tabla","SELECT * FROM tabla","FETCH * tabla","QUERY tabla"), 1),
        ExamQuestion("¿Qué cláusula se usa para filtrar filas?",
            listOf("FILTER","HAVING","WHERE","CONDITION"), 2),
        ExamQuestion("¿Qué operador elimina duplicados en un SELECT?",
            listOf("UNIQUE","NODUPLICATE","DISTINCT","DIFFERENT"), 2),
        ExamQuestion("Para ordenar de MAYOR a MENOR se usa:",
            listOf("ORDER BY col UP","ORDER BY col DESC","SORT BY col","ORDER BY col ASC"), 1),
        ExamQuestion("¿Qué hace LIMIT 5?",
            listOf("Salta los primeros 5","Devuelve solo 5 filas","Cuenta hasta 5","Nada"), 1),
        ExamQuestion("¿Qué operador busca un patrón de texto?",
            listOf("MATCH","SIMILAR","CONTAINS","LIKE"), 3),
        ExamQuestion("¿Cuál es la diferencia entre WHERE y HAVING?",
            listOf("No hay diferencia","WHERE filtra filas; HAVING filtra grupos","HAVING filtra filas; WHERE filtra grupos","HAVING es más rápido"), 1),
        ExamQuestion("INNER JOIN devuelve:",
            listOf("Todas las filas izquierda","Todas las filas ambas tablas","Solo filas con coincidencia en ambas","Solo sin coincidencia"), 2),
        ExamQuestion("Para sumar valores de una columna se usa:",
            listOf("ADD(col)","TOTAL(col)","SUM(col)","PLUS(col)"), 2),
        ExamQuestion("¿Qué pasa con UPDATE sin WHERE?",
            listOf("Da error","Solo actualiza la primera fila","Actualiza TODAS las filas","No hace nada"), 2),
        ExamQuestion("GROUP BY se usa para:",
            listOf("Ordenar resultados","Filtrar filas individuales","Agrupar filas con el mismo valor","Unir tablas"), 2),
        ExamQuestion("¿Cuál es el orden correcto de cláusulas?",
            listOf("FROM→WHERE→SELECT→ORDER BY","SELECT→FROM→WHERE→GROUP BY→HAVING→ORDER BY","SELECT→WHERE→FROM","WHERE→SELECT→FROM"), 1),
        ExamQuestion("BETWEEN 500 AND 5000 es equivalente a:",
            listOf(">500 AND <5000",">=500 AND <=5000","=500 OR =5000",">500 OR <5000"), 1),
        ExamQuestion("¿Qué función cuenta el número de filas?",
            listOf("NUM()","ROWS()","COUNT()","TOTAL()"), 2),
        ExamQuestion("¿Qué hace LEFT JOIN que INNER JOIN no hace?",
            listOf("Une más rápido","Incluye filas sin coincidencia de la tabla izquierda","Filtra duplicados","Ordena automáticamente"), 1)
    )

    // ── Frases motivadoras ────────────────────────────────────
    val motivationalPhrases = listOf(
        "¡Excelente! Cada consulta te hace más poderoso 💪",
        "¡Lo lograste! Estás dominando SQL paso a paso 🚀",
        "¡Perfecto! Tu cerebro ya piensa en SQL 🧠",
        "¡Increíble! Los datos no tienen secretos para ti 🔥",
        "¡Brillante! Así se aprende, con práctica y dedicación ⭐",
        "¡Wow! Esa consulta fue perfecta. ¡Sigue así! 🏆",
        "¡Genial! Cada error superado es una lección aprendida 💎",
        "¡Fantástico! Ya dominas este concepto. Adelante 🎯"
    )
}
