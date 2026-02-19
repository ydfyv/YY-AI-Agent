import Url_env from './Url_env.js'

export const API = {
  BASE_URL: process.env.NODE_ENV === 'production' ? Url_env.prod : Url_env.env,
  TEACHER_ASSISTANT_SSE: `${process.env.NODE_ENV === 'production' ? Url_env.prod : Url_env.env}/ai/teacher-assistant/chat/sse-emitter`,
  YYMANUS_SSE: `${process.env.NODE_ENV === 'production' ? Url_env.prod : Url_env.env}/ai/chat/manus`
}

export const generateChatId = () => {
  return `chat_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`
}
