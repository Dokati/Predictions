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

        if(this.columns < 0 || this.rows < 0){
            throw new IllegalArgumentException("The number of rows and columns of the grid should be positive integers");
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
}