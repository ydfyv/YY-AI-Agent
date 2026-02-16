import { Routes, Route } from 'react-router-dom'
import Home from './pages/Home'
import TeacherAssistant from './pages/TeacherAssistant'
import SuperAgent from './pages/SuperAgent'
import './App.css'

function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/teacher-assistant" element={<TeacherAssistant />} />
      <Route path="/super-agent" element={<SuperAgent />} />
    </Routes>
  )
}

export default App
