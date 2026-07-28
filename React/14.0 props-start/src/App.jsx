
import Header from "./components/Header/Header.jsx";
import MainContent from "./components/MainContent/MainContent.jsx";
import TabButton from "./components/TabButton.jsx";
import Section from "./components/MainContent/Section.jsx";
import {myData, EXAMPLES} from "../data.js"
import {useState} from "react";
import Tabs from "./components/MainContent/Tabs.jsx";

function App() {
  const [selectedTopic, setSelectedTopic] = useState(); 
  //  useState import from react - need to set in the first of main function
  //  useState return an array have 2 values [dataSelected, setData]
  //  function App() will be recall after click - Use to update UI after function changed data
  //  let tabContent = "Nội dung được hiển thị";

  let tabContent = <p>Vui lòng click vào nút để lựa chọn 1 chủ đề</p>;
  if (selectedTopic) {
    tabContent = (
      <div id="tab-content">
        <h3>{EXAMPLES[selectedTopic].title}</h3>
        <p>{EXAMPLES[selectedTopic].desc}</p>
        <pre>
          <code>{EXAMPLES[selectedTopic].code}</code>
        </pre>
      </div>
    );
  }

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
        <Section id="core-concepts" title="Khái niệm chính trong React">
            <ul>
              {
                myData.map((item) => (
                  <MainContent key={item.title} {...item}/>
                ))
              }
          </ul>
        </Section>

        <Section id="examples" title="Examples">
          <Tabs ButtonContainer="menu" button={<>
            <TabButton isSelected={selectedTopic==="components"} onClick={()=>{handleSelect('components')}}>Components</TabButton>
            <TabButton isSelected={selectedTopic==="jsx"} onClick={()=>{handleSelect('jsx')}}>JSX</TabButton>
            <TabButton isSelected={selectedTopic==="props"} onClick={()=>{handleSelect('props')}}>Props</TabButton>
            <TabButton isSelected={selectedTopic==="state"} onClick={()=>{handleSelect('state')}}>State</TabButton>
            </>
          }>
            {tabContent} 
          </Tabs>
          
        </Section>
        

      </main>
    </>
  );
}

export default App;
