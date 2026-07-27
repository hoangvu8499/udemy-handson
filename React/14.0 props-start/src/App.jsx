
import Header from "./components/Header/Header.jsx";
import MainContent from "./components/MainContent/MainContent.jsx";
import TabButton from "./components/TabButton.jsx";
import {myData, EXAMPLES} from "../data.js"
import {useState} from "react";

function App() {
  const [selectedTopic, setSelectedTopic] = useState("components"); 
  //  useState import from react - need to set in the first of main function
  //  useState return an array have 2 values [dataSelected, setData]
  //  function App() will be recall after click - Use to update UI after function changed data
  //  let tabContent = "Nội dung được hiển thị";

  function handleSelect(selectedButton) {
      // alert(`${selectedButton} button clicked`);
      console.log("data change: "+selectedButton);
      setSelectedTopic(selectedButton);
  }


  console.log(myData);
  console.log(EXAMPLES);
  return (
    <>
      <Header />
      <main>
        <section id="core-concepts">
          <h2>Khái niệm chính trong React</h2>
          <ul>
            {/* <MainContent{...myData[0]}/>
            <MainContent{...myData[1]}/>
            <MainContent{...myData[2]}/>
            <MainContent{...myData[3]}/> */}
            {
              myData.map((item) => (
                 <MainContent key={item.title} {...item}/>
              ))
            }
          </ul>
        </section>

        <section id="examples">
          <h2>Examples</h2>
          <menu>
            <TabButton isSelected={selectedTopic==="components"} onSelect={()=>{handleSelect('components')}}>Components</TabButton>
            <TabButton isSelected={selectedTopic==="jsx"} onSelect={()=>{handleSelect('jsx')}}>JSX</TabButton>
            <TabButton isSelected={selectedTopic==="props"} onSelect={()=>{handleSelect('props')}}>Props</TabButton>
            <TabButton isSelected={selectedTopic==="state"} onSelect={()=>{handleSelect('state')}}>State</TabButton>
          </menu>
          <div id="tab-content">
            {/* <h3>{EXAMPLES.components.title}</h3>  */}
            <h3>{EXAMPLES[selectedTopic].title}</h3>
            <p>{EXAMPLES[selectedTopic].desc}</p>
            <pre>
              <code>
                  {EXAMPLES[selectedTopic].code}
              </code>
            </pre>
          </div>
        </section>
        

      </main>
    </>
  );
}

export default App;
