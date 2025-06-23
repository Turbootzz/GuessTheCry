import './assets/css/global.css'
import QuizController from './components/QuizController'
import Navbar from './components/Navbar'
import { AuthProvider } from './context/AuthContext'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Login from './pages/Login'
import Register from './pages/Register'
import Profile from './pages/Profile'
import ProtectedRoute from './components/ProtectedRoute'

export default function App() {
	return (
		<AuthProvider>
			<BrowserRouter>
				<Navbar />
				<div className="flex min-h-screen items-center justify-center p-4">
					<Routes>
						<Route path="/login" element={<Login />} />
						<Route path="/register" element={<Register />} />
						<Route element={<ProtectedRoute />}>
							<Route path="/" element={<QuizController />} />
							<Route path="/profile" element={<Profile />} />
						</Route>
						<Route path="*" element={<div>404 - Page Not Found</div>} />
					</Routes>
				</div>
			</BrowserRouter>
		</AuthProvider>
	)
}
