package agh.ics.oop;

public class World {
    public static void main(String[] args) {
        try {
            MoveDirection[] directions = new OptionsParser().parse(args);
            Vector2d[] positions = { new Vector2d(2,2), new Vector2d(3,4) };

            AbstractWorldMap map = new GrassField(10);

            IEngine engine = new SimulationEngine(map, directions, positions);
            System.out.println(map);
            engine.run();
            System.out.println(map);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println("Program shutting down...");
        }
    }
}
