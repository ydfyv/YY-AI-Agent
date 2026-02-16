import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { TeacherIcon, RobotIcon } from '../components/Icons'
import Footer from '../components/Footer'
import './Home.css'

function Home() {
  const navigate = useNavigate()

  const handleNavigateToTeacherAssistant = () => {
    navigate('/teacher-assistant')
  }

  const handleNavigateToSuperAgent = () => {
    navigate('/super-agent')
  }

  return (
    <div className="home-container">
      <div className="home-header">
        <h1 className="home-title">AI 智能应用中心</h1>
        <p className="home-subtitle">选择您的AI助手</p>
      </div>
      <div className="app-cards">
        <div className="app-card" onClick={handleNavigateToTeacherAssistant}>
          <div className="app-icon"><TeacherIcon /></div>
          <h2 className="app-title">AI 教师助手</h2>
          <p className="app-description">专业的教学辅助工具，帮助您解答教学相关问题</p>
        </div>
        <div className="app-card" onClick={handleNavigateToSuperAgent}>
          <div className="app-icon"><RobotIcon /></div>
          <h2 className="app-title">AI 超级智能体</h2>
          <p className="app-description">全能型AI助手，处理各类复杂任务和问题</p>
        </div>
      </div>
      <Footer />
    </div>
  )
}

export default Home
