package com.sqlmimo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.sqlmimo.ui.screens.*
import com.sqlmimo.ui.theme.SQLMimoTheme
import com.sqlmimo.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm: AppViewModel = viewModel()
            val isDark by vm.isDarkTheme.collectAsState()

            SQLMimoTheme(darkTheme = isDark) {
                SQLAprendeApp(vm = vm)
            }
        }
    }
}

@Composable
fun SQLAprendeApp(vm: AppViewModel) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    val showBottomBar = currentRoute in listOf("home", "reference", "exam", "profile")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    // ← Ya no es Color.White fijo; usa el color de superficie del tema
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp
                ) {
                    listOf(
                        Triple("home", "🏠", "Inicio"),
                        Triple("reference", "📖", "Referencia"),
                        Triple("exam", "🎓", "Examen"),
                        Triple("profile", "👤", "Perfil")
                    ).forEach { (route, icon, label) ->
                        NavigationBarItem(
                            selected = currentRoute == route,
                            onClick = {
                                navController.navigate(route) {
                                    popUpTo("home") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Text(icon, fontSize = 20.sp) },
                            label = { Text(label, fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF4F46E5),
                                selectedTextColor = Color(0xFF4F46E5),
                                indicatorColor = Color(0xFFEEF2FF)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(vm = vm, onModuleClick = { index ->
                    if (index == -1) navController.navigate("exam")
                    else navController.navigate("module/$index")
                })
            }
            composable(
                "module/{index}",
                arguments = listOf(navArgument("index") { type = NavType.IntType })
            ) { backStack ->
                val idx = backStack.arguments!!.getInt("index")
                ModuleScreen(
                    vm = vm,
                    moduleIndex = idx,
                    onLessonClick = { lessonIdx ->
                        navController.navigate("exercise/$idx/$lessonIdx")
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(
                "exercise/{moduleIdx}/{lessonIdx}",
                arguments = listOf(
                    navArgument("moduleIdx") { type = NavType.IntType },
                    navArgument("lessonIdx") { type = NavType.IntType }
                )
            ) { backStack ->
                val mIdx = backStack.arguments!!.getInt("moduleIdx")
                val lIdx = backStack.arguments!!.getInt("lessonIdx")
                val module = vm.modules[mIdx]
                ExerciseScreen(
                    vm = vm,
                    moduleIndex = mIdx,
                    lessonIndex = lIdx,
                    onBack = { navController.popBackStack() },
                    onNext = {
                        if (lIdx + 1 < module.lessons.size) {
                            navController.navigate("exercise/$mIdx/${lIdx + 1}") {
                                popUpTo("exercise/$mIdx/$lIdx") { inclusive = true }
                            }
                        } else {
                            navController.navigate("module/$mIdx") {
                                popUpTo("module/$mIdx") { inclusive = true }
                            }
                        }
                    }
                )
            }
            composable("reference") { ReferenceScreen() }
            composable("exam") { ExamScreen(vm = vm) }
            composable("profile") { ProfileScreen(vm = vm) }
        }
    }
}
