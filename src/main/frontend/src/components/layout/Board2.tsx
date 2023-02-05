import React from "react";
import Typography from "@mui/material/Typography";
import Box from '@mui/material/Box';
import { RxAvatar } from "react-icons/rx";
import { BsBookmarkStar } from "react-icons/bs";
import { TfiCommentAlt } from "react-icons/tfi";
import { board2Items } from "../data/BoardItems";
import "../style/Board.css";

const Board2: React.FC = () => {
    return (
        <>
        <div className="board">
        <Typography variant="h5" className="boardTitle">Q&A 게시판</Typography>
            {board2Items.map((posting, index) => {
                return (
                    <>
                    <Box sx={{ width: 400, height: 130, '&:hover': {
                    backgroundColor: 'gainsboro',
                    opacity: [1.0, 0.8, 0.7],
                    },}} className="box">
                    <p><RxAvatar size={30} className="icon"/></p> 
                    <p className="name">{posting.nickname} · {posting.time}
                    <img className="language" src={posting.language} /> </p>
                    <p className="title">{posting.title}</p>
                    <p className="comment">
                        <TfiCommentAlt size={20}/> {posting.comment}
                    </p>
                    <p className="bookmark">
                        <BsBookmarkStar size={20}/>{posting.bookmark}</p>
                        
                    </Box>
                    </>
                );
            })}
        </div>
        </>
    );
  }
  
  export default Board2;