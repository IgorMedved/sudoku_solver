package sudoku_model.ai;

public class Box {
	public int row;
	public int col;
	
	public Box (int row, int col)
	{
		this.row = row;
		this.col = col;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Box other = (Box) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}
	
	public boolean isValid()
	{
		return (row>=0 && row <9 && col>=0 && col<9);
	}

	public String toString()
	{
		return "box: row " + row + " col " + col;
	}
}
