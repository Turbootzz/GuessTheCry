import { createContext, useContext, useState, useEffect } from 'react'
import type { ReactNode } from 'react'

interface User {
	id: number
	username: string
	role: string
}

interface AuthContextProps {
	user: User | null
	token: string | null
	login: (user: User, token: string) => void
	logout: () => void
}

const AuthContext = createContext<AuthContextProps | undefined>(undefined)

export const AuthProvider = ({ children }: { children: ReactNode }) => {
	const [user, setUser] = useState<User | null>(null)
	const [token, setToken] = useState<string | null>(null)
	const [isLoading, setIsLoading] = useState(true)

	useEffect(() => {
		try {
			const storedToken = localStorage.getItem('token')
			const storedUser = localStorage.getItem('user')

			if (storedToken && storedUser) {
				setUser(JSON.parse(storedUser))
				setToken(storedToken)
			}
		} catch (error) {
			console.error('Failed to parse auth data from localStorage', error)
		} finally {
			setIsLoading(false)
		}
	}, [])

	const login = (user: User, token: string) => {
		setUser(user)
		setToken(token)
		// save in localStorage
		localStorage.setItem('user', JSON.stringify(user))
		localStorage.setItem('token', token)
	}

	const logout = () => {
		setUser(null)
		setToken(null)
		// delete from localStorage
		localStorage.removeItem('user')
		localStorage.removeItem('token')
	}

	const value = { user, token, login, logout }

	if (isLoading) {
		return <div>Loading...</div>
	}

	return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export const useAuth = () => {
	const context = useContext(AuthContext)
	if (!context) throw new Error('useAuth must be used inside AuthProvider')
	return context
}
