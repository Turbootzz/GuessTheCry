import { createContext, useContext, useState, useEffect } from 'react'
import type { ReactNode } from 'react'

interface PokemonContextProps {
	pokemonNames: string[]
	isLoading: boolean
}

const PokemonContext = createContext<PokemonContextProps | undefined>(undefined)

// Een simpele API-functie om de namen op te halen
async function fetchAllPokemonNames(): Promise<string[]> {
	// Gebruik de publieke URL van je backend
	const VITE_API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'
	const response = await fetch(`${VITE_API_URL}/pokemon/names`)
	if (!response.ok) {
		throw new Error('Failed to fetch Pokémon names')
	}
	return response.json()
}

export const PokemonProvider = ({ children }: { children: ReactNode }) => {
	const [pokemonNames, setPokemonNames] = useState<string[]>([])
	const [isLoading, setIsLoading] = useState(true)

	useEffect(() => {
		fetchAllPokemonNames()
			.then(names => {
				setPokemonNames(names)
			})
			.catch(error => {
				console.error(error)
				// here error state for later
			})
			.finally(() => {
				setIsLoading(false)
			})
	}, []) // only loads once when the component mounts

	return (
		<PokemonContext.Provider value={{ pokemonNames, isLoading }}>
			{children}
		</PokemonContext.Provider>
	)
}

export const usePokemon = () => {
	const context = useContext(PokemonContext)
	if (!context) {
		throw new Error('usePokemon must be used within a PokemonProvider')
	}
	return context
}
