package Grid;

import PRD.PRDWorld;

public class Grid {
    private Integer columns;
    private Integer rows;

    public Grid(Integer columns, Integer rows) {
        this.columns = columns;
        this.rows = rows;
    }

    public Grid(PRDWorld.PRDGrid pRDgrid) {
        this.columns = pRDgrid.getColumns();
        this.rows = pRDgrid.getRows();

        if(this.columns < 10 || this.rows < 10 || this.columns > 100 || this.rows > 100){
            throw new IllegalArgumentException("The number of rows and columns of the grid should be between 10 and 100");
        }
    }

    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
    public Integer getSpace()
    {
        return this.columns*this.rows;
    }
}