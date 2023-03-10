import React from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import Home from "./components/pages/Home";
import BoardWrite from "./components/pages/BoardWritePage";
import BoardMain from "./components/pages/Board/Free/BoardMain";
import { freeBoard } from "./components/data/BoardData";
import QnABoard from "./components/pages/Board/QnABoard";
import QnADetail from "./components/pages/Board/QnADetails";
import RecruitBoard from "./components/pages/Board/RecruitBoard";
import MyPage from "./components/pages/MyPage";
import Notice from "./components/pages/Notice";
import ViewPosting from "./components/pages/Board/Free/ViewPosting";

function App() {
  return (
    <>
    <Router>
      <Routes>
        <Route path="/" element={<Home/>}/>
        <Route path="post" element={<BoardWrite/>}/>
        <Route path="/free" element={<BoardMain />} /> 
        <Route path="/free/:id" element={<ViewPosting />} />
        <Route path="/questions" element={<QnABoard/>}/>
        <Route path="/questions/:id" element={<QnADetail/>}/>
        <Route path="/recruit/*" element={<RecruitBoard/>}/>
        <Route path="/mypage" element={<MyPage/>}/>
        <Route path="/notice" element={<Notice/>}/>
      </Routes>
    </Router>
    </>
  )
}

export default App;