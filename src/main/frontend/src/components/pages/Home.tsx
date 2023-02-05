import React from 'react';
import Grid from '@mui/material/Unstable_Grid2';
import Header from '../layout/Header';
import Banner from '../layout/Banner';
import LeftSidebar from '../layout/LeftSidebar';
import RightSidebar from '../layout/RightSidebar';
import Board from "../layout/Board";
import Board2 from "../layout/Board2";

const Home: React.FC = () => {
    return (
      <>
        <Header/>
        <Grid container spacing={2} style={{margin:0}}>   
          <Grid xs>
            <LeftSidebar/>
          </Grid>       
          <Grid xs={7}>
          <Banner/>
            <Grid container spacing={2} style={{margin:0}}>
              <Grid xs>
              <Board/>
              <Board2/>
              </Grid>
              <Grid xs>
              <Board2/>
              <Board/>
              </Grid>
            </Grid>
          </Grid>
          <Grid xs>
          <RightSidebar/>
          </Grid>
        </Grid>
      </>
    );
  }
  
  export default Home;