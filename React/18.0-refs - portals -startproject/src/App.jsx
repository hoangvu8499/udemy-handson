import Header from "./components/Header";
import Player from "./components/Player";
import TimeStopper from "./components/TimeStopper";
function App() {
  return (
    <>
      <Header/>
      <Player/>
      <div id="challenges">
        <TimeStopper title="1" seconds="1"></TimeStopper>
        <TimeStopper title="5" seconds="5"></TimeStopper>
        <TimeStopper title="8" seconds="8"></TimeStopper>
        <TimeStopper title="3" seconds="3"></TimeStopper>
      </div>
      
    </>
  );
}

export default App;
