package Dto;

public class GridDto {
    String rows;
    String Culomns;

    public GridDto(String rows, String culomns) {
        this.rows = rows;
        Culomns = culomns;
    }

    public String getRows() {
        return rows;
    }

    public String getCulomns() {
        return Culomns;
    }
}
