package com.sqlmimo.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sqlmimo.model.ExamQuestion
import com.sqlmimo.ui.components.CodeBlock
import com.sqlmimo.ui.components.XpProgressBar
import com.sqlmimo.ui.theme.Brand
import com.sqlmimo.viewmodel.AppViewModel

// ── Datos de referencia ───────────────────────────────────────
data class RefEntry(val cmd: String, val cat: String, val desc: String, val syntax: String, val example: String, val note: String)

val REFERENCE = listOf(
    RefEntry("SELECT","Consulta","Selecciona columnas de una tabla","SELECT col1, col2 FROM tabla;","SELECT nombre, precio FROM productos;","Usa * para todas las columnas. Usa AS para renombrar."),
    RefEntry("WHERE","Filtro","Filtra filas según una condición","SELECT * FROM tabla WHERE condicion;","SELECT * FROM empleados WHERE depto = 'IT' AND salario > 30000;","Operadores: =, !=, >, <, >=, <=, BETWEEN, LIKE, IN, IS NULL"),
    RefEntry("ORDER BY","Ordenamiento","Ordena los resultados","SELECT * FROM tabla ORDER BY col ASC|DESC;","SELECT * FROM productos ORDER BY precio DESC;","ASC = menor a mayor. DESC = mayor a menor. Puedes ordenar por múltiples columnas."),
    RefEntry("LIMIT","Paginación","Limita el número de filas devueltas","SELECT * FROM tabla LIMIT n;","SELECT * FROM productos ORDER BY precio DESC LIMIT 5;","Útil para mostrar los top N resultados."),
    RefEntry("DISTINCT","Consulta","Elimina duplicados del resultado","SELECT DISTINCT columna FROM tabla;","SELECT DISTINCT ciudad FROM clientes;","Devuelve solo valores únicos."),
    RefEntry("LIKE","Filtro","Busca patrones en texto","SELECT * FROM tabla WHERE col LIKE 'patron';","SELECT * FROM clientes WHERE nombre LIKE 'A%';","% = cualquier texto. _ = un carácter. 'A%' = empieza con A."),
    RefEntry("BETWEEN","Filtro","Filtra un rango de valores","SELECT * FROM tabla WHERE col BETWEEN v1 AND v2;","SELECT * FROM productos WHERE precio BETWEEN 500 AND 5000;","Incluye los extremos. Equivale a >= v1 AND <= v2."),
    RefEntry("IN","Filtro","Filtra por una lista de valores","SELECT * FROM tabla WHERE col IN (v1, v2, v3);","SELECT * FROM empleados WHERE depto IN ('IT','Ventas');","Más limpio que múltiples OR. También puedes usar NOT IN."),
    RefEntry("COUNT","Agregado","Cuenta el número de filas","SELECT COUNT(*) FROM tabla;","SELECT COUNT(*) FROM empleados WHERE depto = 'IT';","COUNT(*) cuenta todas las filas. COUNT(col) ignora NULL."),
    RefEntry("SUM","Agregado","Suma los valores de una columna","SELECT SUM(columna) FROM tabla;","SELECT SUM(total) FROM pedidos;","Ignora NULL. Úsalo con WHERE para sumas parciales."),
    RefEntry("AVG","Agregado","Calcula el promedio","SELECT AVG(columna) FROM tabla;","SELECT AVG(salario) FROM empleados;","Promedio = suma / count. Ignora NULL."),
    RefEntry("MAX / MIN","Agregado","Valor máximo y mínimo","SELECT MAX(col), MIN(col) FROM tabla;","SELECT MAX(precio), MIN(precio) FROM productos;","También funcionan con texto y fechas."),
    RefEntry("GROUP BY","Agrupación","Agrupa filas con el mismo valor","SELECT col, COUNT(*) FROM tabla GROUP BY col;","SELECT depto, AVG(salario) FROM empleados GROUP BY depto;","Toda columna en SELECT debe estar en GROUP BY o ser una función agregada."),
    RefEntry("HAVING","Agrupación","Filtra grupos","SELECT col, COUNT(*) FROM tabla GROUP BY col HAVING COUNT(*) > n;","SELECT depto, COUNT(*) FROM empleados GROUP BY depto HAVING COUNT(*) > 2;","HAVING filtra DESPUÉS de agrupar. WHERE filtra ANTES."),
    RefEntry("INNER JOIN","Join","Combina filas coincidentes de ambas tablas","SELECT * FROM t1 INNER JOIN t2 ON t1.id = t2.fk;","SELECT c.nombre, p.total FROM clientes c INNER JOIN pedidos p ON c.id = p.cliente_id;","Solo devuelve filas con coincidencia en AMBAS tablas."),
    RefEntry("LEFT JOIN","Join","Todas las filas de la tabla izquierda","SELECT * FROM t1 LEFT JOIN t2 ON t1.id = t2.fk;","SELECT c.nombre, p.id FROM clientes c LEFT JOIN pedidos p ON c.id = p.cliente_id;","Incluye filas sin coincidencia. Las columnas de la derecha serán NULL."),
    RefEntry("INSERT INTO","DML","Inserta nuevas filas","INSERT INTO tabla (col1, col2) VALUES (v1, v2);","INSERT INTO productos (nombre, precio) VALUES ('Tablet', 7500);","Puedes insertar múltiples filas con VALUES (v1,v2), (v3,v4)."),
    RefEntry("UPDATE","DML","Modifica filas existentes","UPDATE tabla SET col = valor WHERE condicion;","UPDATE productos SET precio = 400 WHERE id = 2;","¡SIEMPRE usa WHERE! Sin WHERE se actualizan TODAS las filas."),
    RefEntry("DELETE","DML","Elimina filas de una tabla","DELETE FROM tabla WHERE condicion;","DELETE FROM empleados WHERE id = 5;","¡SIEMPRE usa WHERE! Sin WHERE se eliminan TODAS las filas."),
    RefEntry("CREATE TABLE","DDL","Crea una nueva tabla","CREATE TABLE tabla (col1 TIPO restricciones, ...);","CREATE TABLE usuarios (id INT PRIMARY KEY, nombre VARCHAR(100) NOT NULL);","Tipos: INT, VARCHAR(n), TEXT, DECIMAL, DATE, BOOLEAN."),
    RefEntry("CASE","Avanzado","Lógica condicional en SQL","SELECT CASE WHEN cond THEN v1 ELSE v2 END FROM tabla;","SELECT nombre, CASE WHEN salario >= 35000 THEN 'Senior' ELSE 'Junior' END AS nivel FROM empleados;","Como un if/else. Puedes encadenar múltiples WHEN."),
    RefEntry("Subconsulta","Avanzado","Consulta dentro de otra consulta","SELECT * FROM tabla WHERE col > (SELECT AVG(col) FROM tabla);","SELECT * FROM empleados WHERE salario > (SELECT AVG(salario) FROM empleados);","Se ejecutan de adentro hacia afuera."),
)

val CAT_COLORS = mapOf(
    "Consulta" to Pair(Color(0xFFEEF2FF), Color(0xFF3730A3)),
    "Filtro" to Pair(Color(0xFFEFF6FF), Color(0xFF1D4ED8)),
    "Ordenamiento" to Pair(Color(0xFFF0FDF4), Color(0xFF166534)),
    "Paginación" to Pair(Color(0xFFF0FDF4), Color(0xFF166534)),
    "Agregado" to Pair(Color(0xFFFFFBEB), Color(0xFF92400E)),
    "Agrupación" to Pair(Color(0xFFFFF7ED), Color(0xFF9A3412)),
    "Join" to Pair(Color(0xFFFDF4FF), Color(0xFF6B21A8)),
    "DML" to Pair(Color(0xFFFEF2F2), Color(0xFF991B1B)),
    "DDL" to Pair(Color(0xFFF0FDFA), Color(0xFF134E4A)),
    "Avanzado" to Pair(Color(0xFFFDF4FF), Color(0xFF7E22CE)),
)

@Composable
fun ReferenceScreen() {
    var expandedCmd by remember { mutableStateOf<String?>(null) }
    val categories = REFERENCE.map { it.cat }.distinct()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("Referencia SQL", fontSize = 22.sp, fontWeight = FontWeight.Medium)
            Text(
                "Todos los comandos con ejemplos",
                fontSize = 13.sp, color = Color(0xFF6B7280),
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        categories.forEach { cat ->
            item {
                Text(
                    cat.uppercase(),
                    fontSize = 11.sp, fontWeight = FontWeight.Medium,
                    color = Color(0xFF9CA3AF),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFE5E7EB))
            }

            val entries = REFERENCE.filter { it.cat == cat }
            items(entries) { ref ->
                val isExpanded = expandedCmd == ref.cmd
                val catColor = CAT_COLORS[cat] ?: Pair(Color(0xFFF3F4F6), Color(0xFF374151))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(0.5.dp, Color(0xFFE5E7EB)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column {
                        // Header clickable
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedCmd = if (isExpanded) null else ref.cmd }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        ref.cmd,
                                        fontWeight = FontWeight.Medium, fontSize = 14.sp,
                                        color = Brand,
                                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(catColor.first)
                                            .padding(horizontal = 6.dp, vertical = 1.dp)
                                    ) {
                                        Text(cat, fontSize = 10.sp, color = catColor.second, fontWeight = FontWeight.Medium)
                                    }
                                }
                                Text(ref.desc, fontSize = 12.sp, color = Color(0xFF6B7280))
                            }
                            Text(if (isExpanded) "▲" else "▼", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                        }

                        // Cuerpo expandible
                        if (isExpanded) {
                            HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFE5E7EB))
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text("Sintaxis:", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF374151))
                                CodeBlock(ref.syntax)
                                Text("Ejemplo:", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF374151))
                                CodeBlock(ref.example)
                                if (ref.note.isNotEmpty()) {
                                    Surface(color = Color(0xFFEEF2FF), shape = RoundedCornerShape(8.dp)) {
                                        Row(modifier = Modifier.padding(10.dp)) {
                                            Text("💡 ", fontSize = 12.sp)
                                            Text(ref.note, fontSize = 12.sp, color = Color(0xFF3730A3), lineHeight = 18.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        item { Spacer(Modifier.height(80.dp)) }
    }
}

// ── Examen Final ──────────────────────────────────────────────
@Composable
fun ExamScreen(vm: AppViewModel) {
    val allDone = vm.allModulesCompleted()
    val examDone by vm.examDone.collectAsState()
    val examScore by vm.examScore.collectAsState()

    if (!allDone) {
        val completedIds by vm.completedIds.collectAsState()
        val done = vm.modules.filter { m -> m.lessons.all { completedIds.contains(it.id) } }.size
        Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(0.5.dp, Color(0xFFE5E7EB)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🔒", fontSize = 48.sp)
                    Spacer(Modifier.height(12.dp))
                    Text("Examen bloqueado", fontWeight = FontWeight.Medium, fontSize = 18.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Completa todos los módulos para desbloquear el examen final.",
                        fontSize = 14.sp, color = Color(0xFF6B7280),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Spacer(Modifier.height(14.dp))
                    LinearProgressIndicator(
                        progress = { done.toFloat() / vm.modules.size },
                        modifier = Modifier.fillMaxWidth().height(7.dp).clip(RoundedCornerShape(4.dp)),
                        color = Brand,
                        trackColor = Color(0xFFE5E7EB)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("$done / ${vm.modules.size} módulos completados", fontSize = 13.sp, color = Color(0xFF6B7280))
                }
            }
        }
        return
    }

    var currentQ by remember { mutableStateOf(0) }
    var answers by remember { mutableStateOf(listOf<Boolean>()) }
    var selectedOpt by remember { mutableStateOf<Int?>(null) }
    var showResult by remember { mutableStateOf(examDone) }
    var finalScore by remember { mutableStateOf(examScore) }

    if (showResult) {
        ExamResultScreen(score = finalScore, total = vm.examQuestions.size, onRetry = {
            currentQ = 0; answers = listOf(); selectedOpt = null; showResult = false; finalScore = 0
        })
        return
    }

    val q = vm.examQuestions[currentQ]
    val progress = currentQ.toFloat() / vm.examQuestions.size

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Examen Final", fontSize = 22.sp, fontWeight = FontWeight.Medium)
            Text("Pregunta ${currentQ + 1} de ${vm.examQuestions.size}", fontSize = 13.sp, color = Color(0xFF6B7280))
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(4.dp)),
                color = Brand,
                trackColor = Color(0xFFE5E7EB)
            )
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(0.5.dp, Color(0xFFE5E7EB)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("PREGUNTA ${currentQ + 1}", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color(0xFF6B7280))
                    Spacer(Modifier.height(8.dp))
                    Text(q.question, fontSize = 15.sp, lineHeight = 24.sp)
                    Spacer(Modifier.height(16.dp))
                    q.options.forEachIndexed { idx, opt ->
                        val sel = selectedOpt
                        val bgColor = when {
                            sel == null -> Color(0xFFF9FAFB)
                            idx == q.correctIndex -> Color(0xFFEAF3DE)
                            idx == sel && sel != q.correctIndex -> Color(0xFFFCEBEB)
                            else -> Color(0xFFF9FAFB)
                        }
                        val borderColor = when {
                            sel == null -> Color(0xFFE5E7EB)
                            idx == q.correctIndex -> Color(0xFFC0DD97)
                            idx == sel && sel != q.correctIndex -> Color(0xFFF7C1C1)
                            else -> Color(0xFFE5E7EB)
                        }
                        OutlinedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .then(if (sel == null) Modifier.clickable { selectedOpt = idx } else Modifier),
                            colors = CardDefaults.cardColors(containerColor = bgColor),
                            border = BorderStroke(0.5.dp, borderColor),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "${('A' + idx)}) $opt",
                                modifier = Modifier.padding(12.dp),
                                fontSize = 13.sp,
                                color = when {
                                    sel != null && idx == q.correctIndex -> Color(0xFF3B6D11)
                                    sel == idx && sel != q.correctIndex -> Color(0xFFA32D2D)
                                    else -> Color(0xFF374151)
                                }
                            )
                        }
                    }
                }
            }
        }

        if (selectedOpt != null) {
            item {
                Button(
                    onClick = {
                        val newAnswers = answers + (selectedOpt == q.correctIndex)
                        if (currentQ < vm.examQuestions.size - 1) {
                            answers = newAnswers
                            currentQ++
                            selectedOpt = null
                        } else {
                            val score = (newAnswers.count { it }.toFloat() / vm.examQuestions.size * 100).toInt()
                            finalScore = score
                            vm.saveExamResult(score)
                            showResult = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Brand),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        if (currentQ < vm.examQuestions.size - 1) "Siguiente →" else "Ver resultado",
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
fun ExamResultScreen(score: Int, total: Int, onRetry: () -> Unit) {
    val correct = (score / 100f * total).toInt()
    val (grade, emoji, msg) = when {
        score >= 90 -> Triple("SQL Experto", "🏆", "¡Dominio total! Eres un maestro del SQL.")
        score >= 70 -> Triple("SQL Avanzado", "🎓", "¡Muy bien! Tienes sólidos conocimientos de SQL.")
        score >= 50 -> Triple("SQL Intermedio", "📚", "Buen intento. Repasa los módulos intermedios.")
        else -> Triple("Sigue practicando", "💪", "No te rindas. Vuelve a los módulos y practica más.")
    }
    val gradeColor = when {
        score >= 90 -> Color(0xFF059669)
        score >= 70 -> Brand
        score >= 50 -> Color(0xFFD97706)
        else -> Color(0xFFDC2626)
    }

    Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(0.5.dp, Color(0xFFE5E7EB)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(emoji, fontSize = 52.sp)
                Spacer(Modifier.height(12.dp))
                Text("$score%", fontSize = 52.sp, fontWeight = FontWeight.Medium, color = gradeColor)
                Text("$correct de $total correctas", fontSize = 14.sp, color = Color(0xFF6B7280))
                Spacer(Modifier.height(12.dp))
                Text(grade, fontSize = 22.sp, fontWeight = FontWeight.Medium, color = gradeColor)
                Spacer(Modifier.height(8.dp))
                Text(msg, fontSize = 14.sp, color = Color(0xFF374151))
                if (score >= 70) {
                    Spacer(Modifier.height(12.dp))
                    Surface(color = Color(0xFFEEF2FF), shape = RoundedCornerShape(8.dp)) {
                        Text(
                            "¡Felicidades! Completaste el curso de SQL. ¡Sigue así! 🚀",
                            modifier = Modifier.padding(12.dp),
                            fontSize = 13.sp, color = Color(0xFF3730A3),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(containerColor = Brand),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Repetir examen", fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

// ── Perfil se ha movido a Profilescreen.kt ───────────────────

@Composable
fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.5.dp, Color(0xFFE5E7EB)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, fontSize = 28.sp, fontWeight = FontWeight.Medium)
            Text(label, fontSize = 12.sp, color = Color(0xFF6B7280))
        }
    }
}

@Composable
fun AchievementCard(ico: String, name: String, earned: Boolean, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = if (earned) Color(0xFFF0FDF4) else Color.White),
        border = BorderStroke(0.5.dp, if (earned) Color(0xFFC0DD97) else Color(0xFFE5E7EB)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(if (earned) ico else "🔒", fontSize = 24.sp)
            Spacer(Modifier.height(4.dp))
            Text(name, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
    }
}
