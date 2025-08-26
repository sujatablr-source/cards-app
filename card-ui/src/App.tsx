import { useState } from 'react'
import './App.css'
import CardForm from './components/CardForm'


function App() {
  return (
    <>
      <div className="min-h-screen flex items-center justify-center bg-gray-100 p-4">
        <div className="bg-white p-6 rounded shadow-md w-full max-w-md">
          <h1 className="text-2xl font-bold mb-4 text-center">Card Form</h1>
          <CardForm />
        </div>
      </div>
    </>
  )
}

export default App
