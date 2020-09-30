package mutomvo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import mutomvo.Exceptions.MutomvoException;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import mutomvo.Utils.Utils;
import mutomvo.TabbedPanels.MutationPanel;
import mutomvo.TabbedPanels.dataClasses.CommandLineOps;

/**t
 * The main class of the application.
 */
public class MutomvoGUIApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
                
        // Check projects folder
        checkProjectsFolder();

        // Check if home path is already set, else... exit!      
        show(new MutomvoGUIView(this));                       
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     * 
     * @param root Root window
     */
    @Override protected void configureWindow(java.awt.Window root){
    }

    /**
     * A convenient static getter for the application instance.
     * 
     * @return the instance of ICanCloudGUIApp
     */
    public static MutomvoGUIApp getApplication() {
        return Application.getInstance(MutomvoGUIApp.class);
    }

    
    /**
     * Main method launching the application.
     * 
     * @param args Arguments
     */
    public static void main(String[] args) throws IOException{
        
        MutationPanel mutation;
        CommandLineOps cmd;
        String projectName, path;        
        //String result;
        OptionParser parser;
        OptionSet options;
        
        
        // No arguments... launching GUI!
        if (args.length == 0){
            System.out.println ("Executing in GUI mode.");
            System.out.println ("type \"java -jar mutomvo.jar -help\" to see the available options\n");                      
            launch(MutomvoGUIApp.class, args);        
        }
        
        else{
            
            cmd = new CommandLineOps();
            
            // Create the parser
            parser = new OptionParser();            
            
            // Show a specific project
            parser.acceptsAll(Arrays.asList("s", "show"), "Shows the configuration of the given project.").withRequiredArg();
            
            // Shows the list of existing projects
            parser.acceptsAll(Arrays.asList("a", "showAll"), "Shows the list of the existing projects.");            
            
            // Sets project folder path
            parser.accepts("setProgramPath", "Sets the path of the source program to be mutated in the specified project.").withRequiredArg(); 
            
            // Sets source program path
            parser.accepts("setProjectPath", "Sets the path where the project is located.").withRequiredArg();
            
            // Generate mutants for the given project
            parser.acceptsAll(Arrays.asList("g", "generateMutants"), "Generate mutants for the specified project.");
            
            // Generate tests for the given project
            parser.acceptsAll(Arrays.asList("t", "generateTests"), "Generate tests for the specified project."); 
            
            // Execute the testing process
            parser.acceptsAll(Arrays.asList("e", "excuteTesting"), "Executes the testing process for the specified project.");       
            
            // Project name
            parser.accepts("p", "Project name.").requiredIf("setProgramPath", "setProjectPath", "g", "t", "e").withRequiredArg();
            
            // Show help
            parser.acceptsAll(Arrays.asList("h", "help"), "Shows help.").forHelp();            
            
            
            try{            
                
                // Parses input arguments
                options = parser.parse(args);  
                
                // Shows help
                if (options.has("h")){    
                    parser.printHelpOn(System.err);                
                }
                
                // Shows the information of a specific project
                else if (options.has("s")){                   
                    System.out.println (cmd.getProjectConfig((String) options.valueOf("s")));
                }                
                
                // Shows the list of the existing projects
                else if (options.has("a")){
                    System.out.println (cmd.getExistingProjects());
                }                
                
                // Sets the project path                
                else if (options.has("setProjectPath")){             
                    projectName = (String) options.valueOf("p");
                    path = (String) options.valueOf("setProjectPath");
                    System.out.println (cmd.setProjectFolderPath(projectName, path));
                }
                    
                // Sets the source program path                
                else if (options.has("setProgramPath")){             
                    projectName = (String) options.valueOf("p");
                    path = (String) options.valueOf("setProgramPath");
                    System.out.println (cmd.setSourceProgramPath(projectName, path));
                }
                
                // Generate mutants
                else if (options.has("g")){ 
                    projectName = (String) options.valueOf("p");  
                    System.out.println("Generating mutants now...");
                    projectName = (String) options.valueOf("p");
                    System.out.println (cmd.generateMutants(projectName));                    
                    System.out.println("Mutants generation end");
                }
                
                // Generate tests
                else if (options.has("t")){ 
                    projectName = (String) options.valueOf("p");  
                    System.out.println("Generating tests now...");
                    projectName = (String) options.valueOf("p");
                    System.out.println (cmd.generateTests(projectName));                    
                    System.out.println("Test generation end");                    
                }
                
                // Execute mutation testing process
                else if (options.has("e")){ 
                }
            }
            
            catch (MutomvoException e){
                System.out.println(e.getMessage());                
            }
            catch (FileNotFoundException e){
                System.out.println(e.getMessage());                
            }
            catch (IOException e){
                System.out.println(e.getMessage());                
            }
            catch (Exception e){                
                System.out.println(e.getMessage());
                System.out.println ("\nUsage:\n");
                parser.printHelpOn(System.err);
            }            
        }
    }
    
    /**
     * Checks the folders for allocating the existing projects. If this folder does not exist,
     * then it will be created.
     * 
     */
    private static void checkProjectsFolder(){
    
        File file;    
    
            // Check if exists the configuration folder!
            file = new File (Utils.projectsFolder);

            if (!file.exists()){
                file.mkdir();        
            }
    }
 

}
