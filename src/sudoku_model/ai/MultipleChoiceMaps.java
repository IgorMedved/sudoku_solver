package sudoku_model.ai;

import java.util.LinkedHashMap;
import java.util.Map;

public class MultipleChoiceMaps {
	
	public static Map<Box, Boolean> getUsedValuesMap()
	{
		Map<Box,Boolean> usedValueMap = new LinkedHashMap<>();
		for (int element = 0; element < 9; element ++)
			for (int value = 1; value<10; value++)
				usedValueMap.put(new Box(element, value), false);
		
		return usedValueMap;
	}
	
	
	public static int squareNumber (int row, int col)
	{
		return row/3*3+ col/3;
	}
	
	
	public static void printMap ()
	{
		Map<Box, Boolean> map= getUsedValuesMap();
		for (Box element: map.keySet())
			System.out.println("Element: " + element.row + " value "+  element.col + " boolean " + map.get(element));
	}
	

}

