import { createContext, useContext, useState } from 'react'
import type { ReactNode } from 'react'

interface User {
	id: number
	username: string
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

	const login = (user: User, token: string) => {
		setUser(user)
		setToken(token)
	}

	const logout = () => {
		setUser(null)
		setToken(null)
	}

	return (
		<AuthContext.Provider value={{ user, token, login, logout }}>
			{children}
		</AuthContext.Provider>
	)
}

export const useAuth = () => {
	const context = useContext(AuthContext)
	if (!context) throw new Error('useAuth must be used inside AuthProvider')
	return context
}
