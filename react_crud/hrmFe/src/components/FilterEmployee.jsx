

export default function FilterEmployee(
        {   filterObject, 
            handleFilterChange, 
            handleSearch,
            setFilterObject,
            categories}
    ) {

    return (
        <>
            <div style={{ margin: "15px 0" }}>
              <input
                  type="text"
                  name="empName"
                  placeholder="Employee Name"
                  value={filterObject.empName}
                  onChange={handleFilterChange}
                  style={{ marginRight: "10px" }}
              />

              <select
                  name="catId"
                  value={filterObject.catId}
                  onChange={handleFilterChange}
                  style={{ marginRight: "10px" }}
              >
                  <option value="">All Category</option>

                  {categories.map((category) => (
                      <option
                          key={category.catId}
                          value={category.catId}
                      >
                          {category.catName}
                      </option>
                  ))}
              </select>

              <button onClick={handleSearch}>
                  Search
              </button>

              <button
                  style={{ marginLeft: "10px" }}
                  onClick={() => {
                      setFilterObject({
                          empName: "",
                          catId: ""
                      });
                  }}
              >
                  Clear
              </button>
            </div>
        </>
    )
}
