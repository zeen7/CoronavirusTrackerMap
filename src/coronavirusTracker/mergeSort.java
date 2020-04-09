/*
 * Merge sort for finding the top 5 COVID-19 cases in the data set and getting their title
 */
package coronavirusTracker;

import java.io.IOException;

public class mergeSort {
	
	//Method used for finding the position of the element after the sort
    public static int getIndexInSortedArray(int arr[], int n, int index) 
    { 
        /*  Count of elements smaller than  
        current element plus the equal element 
        occurring before given index*/
        int result = 0; 
        for (int i = 0; i < n; i++) { 
  
            // If element is smaller then 
            // increase the smaller count 
            if (arr[i] < arr[index]) 
                result++; 
  
            // If element is equal then increase 
            // count only if it occurs before 
            if (arr[i] == arr[index] && i < index) 
                result++; 
        } 
        return result; 
    } 
    
    public void merge(int [] leftArray, int [] rightArray, int [] mergedArray)
    {
    	int leftLength = leftArray.length;
    	int rightLength = rightArray.length;
    	int i = 0; //index for leftArray
    	int j = 0; //index for rightArray
    	int k = 0; //index for mergedArray
    	
    	while(i<leftLength && j<rightLength)
    	{
    		if(leftArray[i] < rightArray[j])
    		{
    			mergedArray[k] = leftArray[i];
    			i++;
    		}
    		else
    		{
    			mergedArray[k] = rightArray[j];
    			j++;
    		}
    		k++;
    	}
    	
    	while(i<leftLength)
    	{
    		mergedArray[k] = leftArray[i];
			i++;
			k++;
    	}
    	while(j<rightLength)
    	{
    		mergedArray[k] = rightArray[j];
			j++;
			k++;
    	}
    }
    public void sort(int [] arr)
    {
    	int n = arr.length;
    	//base case
    	if(n<2)
    	{
    		return;
    	}
    	int mid = n/2;
    	int [] left = new int [mid];
    	int [] right = new int [n-mid];
    	for(int i=0; i<mid ;i++)
    	{
    		left[i]=arr[i];
    	}
    	for(int i=mid; i<n; i++)
    	{
    		right[i-mid]=arr[i];
    	}
    	sort(left);
    	sort(right);
    	merge(left, right, arr);
    }
    
    // Driver code to test above methods 
    public static void main(String[] args) 
    { 
    	try {
			Parse_feed.readFile();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
    	Parse_feed.locationFeatures();
        int arr[] = Parse_feed.getTopCases();
        int arrLength = arr.length; 
        String [] topCasesTitles=Parse_feed.getTopCasesTitle();
        int [] sortedPositions=new int [arrLength];
         
        for(int j=0; j<arr.length; j++)
        {
        	sortedPositions[j]=getIndexInSortedArray(arr, arrLength, j); 
        }
        
        mergeSort a= new mergeSort();
        a.sort(arr);

        String [] newTitle=new String [arrLength];
        for(int i=0; i<arrLength; i++)
        {
        	newTitle[sortedPositions[i]]=topCasesTitles[i];
        }
        
        //top 5 most cases
        for(int i=arr.length-1; i>arr.length-6 ;i--)
        {
        	System.out.println(newTitle[i]+", "+arr[i]);
        }
    } 
}
