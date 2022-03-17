package main.java;

/**
 * TO DO LIST:
 * TODO - Logging
 * TODO - DOCUMENTATION!!!!!!!!
 * TODO - Thread pooling
 * TODO - Exception Handling (properly)
 * TODO - Move DB creds to separate file (better security)
 * TODO - CLEAN UP CODE
 * TODO - Packages
 * TODO - System Initialization Script
 * TODO - Time Trigger Script
 * TODO - Find remote home for the system (i.e. remote server to deploy ETL + DB)
 */


public class Driver
{
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