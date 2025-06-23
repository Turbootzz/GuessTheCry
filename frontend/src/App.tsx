import './assets/css/global.css'
import QuizCard from './components/QuizController'
import Navbar from './components/Navbar'
import { AuthProvider } from './context/AuthContext'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Login from './pages/Login'
import Register from './pages/Register'
import Profile from './pages/Profile'

export default function App() {
	return (
		<AuthProvider>
			<BrowserRouter>
				<Navbar />
				<div className="flex min-h-screen items-center justify-center p-4">
					<Routes>
						<Route path="/" element={<QuizCard />} />
						<Route path="/login" element={<Login />} />
						<Route path="/register" element={<Register />} />
						<Route path="/profile" element={<Profile />} />
					</Routes>
				</div>
			</BrowserRouter>
		</AuthProvider>
	)
}
