import generators.EmployeesGenerator;

public class Main {
    public static void main(String[] args){
        if(args.length < 2){
            System.out.println();
        }

        String fileCode = args[0];
        Integer numberOfEmployees = Integer.parseInt(args[1]);



        EmployeesGenerator.generate(fileCode, numberOfEmployees);
    }
}
