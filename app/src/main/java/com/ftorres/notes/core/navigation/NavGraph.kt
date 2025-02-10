package com.ftorres.notes.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ftorres.notes.presentation.screens.auth.LoginScreen
import com.ftorres.notes.presentation.screens.auth.RegisterScreen
import com.ftorres.notes.presentation.screens.notes.NoteDetailScreen
import com.ftorres.notes.presentation.screens.notes.NotesListScreen
import com.ftorres.notes.presentation.screens.map.MapScreen
import com.ftorres.notes.presentation.viewmodel.AuthViewModel
import com.ftorres.notes.presentation.viewmodel.MapViewModel
import com.ftorres.notes.presentation.viewmodel.NotesViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    notesViewModel: NotesViewModel,
    mapViewModel: MapViewModel,
    onGoogleSignIn: () -> Unit
) {
    val startDestination = if (authViewModel.isUserLoggedIn()) {
        Destinations.NotesList.route
    } else {
        Destinations.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Destinations.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { navController.navigate(Destinations.NotesList.route) },
                onNavigateToRegister = { navController.navigate(Destinations.Register.route) },
                onGoogleSignIn = onGoogleSignIn
            )
        }

        composable(Destinations.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = { navController.navigate(Destinations.NotesList.route) },
                onNavigateToLogin = { navController.navigate(Destinations.Login.route) }
            )
        }

        composable(Destinations.NotesList.route) {
            NotesListScreen(
                viewModel = notesViewModel,
                navController = navController,
                onAddNote = { navController.navigate(Destinations.NoteDetail.route) }
            )
        }

        composable(Destinations.NoteDetail.route) {
            NoteDetailScreen(
                viewModel = notesViewModel,
                mapViewModel = mapViewModel,
                onBack = { navController.popBackStack() },
                navController = navController
            )
        }

        composable(Destinations.Map.route) {
            MapScreen(
                viewModel = mapViewModel,
                navController = navController
            )
        }
    }
}
