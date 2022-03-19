package main.java;


/**
 * The main driver class for the overall system
 *
 *
 *  @author Garrett Kamila Crayton
 *  @version 1.0
 */
public class ETLMain
{
    /**
     * The main function of the entire system (actually runs everything)
     *
     * @param args
     */
    public static void main(String[] args)
    {
        try
        {
            ExtractMain extract = new ExtractMain();

            extract.extract();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}