package com.sqlmimo.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sqlmimo.model.TableData
import com.sqlmimo.ui.theme.*

// ── DataTable ─────────────────────────────────────────────────
@Composable
fun DataTable(table: TableData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        // Usamos surface del tema en lugar de White fijo
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    // Color de fondo sutil que se adapta
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "tabla: ${table.name}",
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = FontFamily.Monospace
                    )
                )
            }
            HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)

            Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                Column {
                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                            .padding(horizontal = 4.dp)
                    ) {
                        table.columns.forEach { col ->
                            Text(
                                text = col,
                                modifier = Modifier
                                    .widthIn(min = 80.dp)
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }
                    HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)

                    table.rows.forEachIndexed { idx, row ->
                        Row(
                            modifier = Modifier
                                .background(
                                    if (idx % 2 == 0) MaterialTheme.colorScheme.surface
                                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)
                                )
                                .padding(horizontal = 4.dp)
                        ) {
                            row.forEach { cell ->
                                Text(
                                    text = cell,
                                    modifier = Modifier
                                        .widthIn(min = 80.dp)
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    style = TextStyle(fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── SqlEditor ─────────────────────────────────────────────────
@Composable
fun SqlEditor(
    value: String,
    onValueChange: (String) -> Unit,
    onRun: () -> Unit,
    onHint: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Editor SQL",
                    style = TextStyle(fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedButton(
                        onClick = onHint,
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Text("💡 Pista", fontSize = 11.sp)
                    }
                }
            }

            HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 100.dp)
                    .padding(14.dp)
            ) {
                if (value.isEmpty()) {
                    Text(
                        "Escribe tu consulta SQL aquí...",
                        style = TextStyle(fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), fontFamily = FontFamily.Monospace)
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    // Agregamos cursorBrush para que el cursor se vea en modo oscuro
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    textStyle = TextStyle(
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 20.sp
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Button(
                onClick = onRun,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Brand)
            ) {
                Text("▶  Ejecutar consulta", color = Color.White)
            }
        }
    }
}

// ── Feedback Card ─────────────────────────────────────────────
@Composable
fun FeedbackCard(
    isCorrect: Boolean,
    title: String,
    body: String,
    motivation: String = "",
    onNext: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    // Aquí podrías definir colores específicos en tu Theme.kt para Success y Error,
    // pero por ahora mantengamos la lógica de colores que tenías adaptada:
    val bgColor = if (isCorrect) SuccessGreenBg else ErrorRedBg

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, color = if(isCorrect) SuccessGreen else ErrorRed)
            Text(body, color = if(isCorrect) SuccessGreen else ErrorRed)

            if (onNext != null) {
                Button(onClick = onNext, modifier = Modifier.padding(top = 8.dp)) {
                    Text("Siguiente")
                }
            }
        }
    }
}

// ── XP Progress Bar ───────────────────────────────────────────
@Composable
fun XpProgressBar(level: Int, xpProgress: Float, xp: Int, xpNeeded: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Nivel $level", color = MaterialTheme.colorScheme.onSurface)
                Text("$xp / $xpNeeded XP", color = MaterialTheme.colorScheme.onSurface)
            }
            LinearProgressIndicator(
                progress = { xpProgress },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = Brand,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}
// ── CodeBlock (Agrégalo a ui/Components.kt) ───────────────────
@Composable
fun CodeBlock(code: String) {
    Surface(
        color = CodeBg, // Usa el color que ya tienes definido en tu Theme.kt
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = code,
            modifier = Modifier.padding(12.dp),
            style = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        )
    }
}