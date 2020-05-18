package ija_proj;


import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static java.time.temporal.ChronoUnit.MILLIS;

/**
 * Kontroler pre simulacie pohybu
 * @author Matej Cimmerman (xcimme00), Daniel Paulovič (xpaulo04)
 */
public class MoveController {

    // Simulation
    private Timer timer;
    public Timer oneTimer;
    private boolean simulationState = false;
    private LocalTime simulationTime;
    private int moveUpdateSpeed = 1000; //ms
    private boolean simulationTask = false;
    private LocalTime previousRealTime;
    private int updateSpeed = 1; //s
    private Path selectedPath;
    private StreetMap streetMap;
    private List<Line> lines = new ArrayList<>();
    private guiClass gui;

    /**
     * Vráti zoznam liniek
     * @return zoznam
     */
    public List<Line> getLines() {
        return lines;
    }

    /**
     * Inicializácia simulácie pohybu vozidiel
     * Obsahuje nový Timer pre  operácie, ktoré nemajú byť ovplyvnené rýchlosťou simulácie
     * @param streetMap mapa zo súboru
     * @param gui GUI kontroler
     * @throws Exception e
     */
    public MoveController(StreetMap streetMap, guiClass gui) throws Exception {
        this.streetMap = streetMap;
        this.gui = gui;
        this.lines = LinesLoader.load(this.streetMap);
        this.updateTraffic();

        this.oneTimer = new Timer("1s");
        this.oneTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                highlightCounter++;
                if (highlightCounter == 4) {
                    gui.clearHighlight();
                }
            }
        }, 0, 1000);
    }

    /**
     * VypoCťia rozdiel dvoch časov
     * @param diff1
     * @param diff2
     * @return rozdiel
     */
    private int minusLocalTime(LocalTime diff1, LocalTime diff2) {
        LocalTime diff = diff1.minusHours(diff2.getHour())
                .minusMinutes(diff2.getMinute())
                .minusSeconds(diff2.getSecond());

        return (diff.getHour() * 60 * 60) + (diff.getMinute() * 60) + (diff.getSecond());
    }


    private int highlightCounter = 0;

    /**
     * Vykreslí vozidlá na mapu podľa toho, ak tam majú v danom Čase byŤ
     * Draw vehicle position in map.
     * @param path
     * @param line
     */
    private void drawActiveVehicle(Path path, Line line) {
        if (path.getTimetable().isEmpty()) {
            return;
        }
        LocalTime tmp1 = path.getTimetable().get(0);
        LocalTime tmp2 = path.getTimetable().get(path.getTimetable().size() - 1);

        if (simulationTime.isBefore(tmp1) || simulationTime.isAfter(tmp2)) {
            return;
        }
        List<LocalTime> timeTable = path.getTimetable();
        for (int i = 0; i < timeTable.size() - 1; i++) {
            LocalTime firstTime = timeTable.get(i);
            LocalTime secondTime = timeTable.get(i + 1);
            if (!(simulationTime.isBefore(firstTime) || simulationTime.isAfter(secondTime))) {
                Coordinate currentTripPosition = TripSimulation.dotPosition(this.simulationTime, path.getTimetable().get(i), path.getTimetable().get(i + 1), line.getStopByIndex(i), line.getStopByIndex(i + 1), line);
                Circle circle = this.gui.createDot(currentTripPosition);
                path.setCircle(circle);
                circle.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    /**
                     * Event when mouse hover on vehicle position.
                     * Highlight line and stops names.
                     * Show vehicle timetable.
                     * @param event
                     */
                    @Override
                    public void handle(MouseEvent event) {
                        gui.highlightLine(line);
                        highlightCounter = 0;
                        gui.clearTripTimetable();
                        gui.showTimetable(path);
                        selectedPath = path;
                    }
                });
                break;
            }
        }
    }

    /**
     * Handle all existing lines.
     * @param line
     */
    private void activeLine(Line line) {
        for (Path path : line.getLineConnections()) {
            drawActiveVehicle(path, line);
        }
    }

    /**
     * Core simulator function.
     * Called every X seconds to refresh position of vehicles.
     */
    private void positionsUpdate() {
        gui.clearSimulationGui();
        for (Line line : lines) {
            activeLine(line);
        }
    }

    /**
     *  Funkcia nastavuje rýchlosť simulácie pohybu vozidiel
     */
    private void positionsSpeed() {
        simulationTime = simulationTime.plus(this.moveUpdateSpeed, MILLIS);
    }

    /**
     * Funkcia vypočíta nové časy pre stopky na základe obmedzení
     * @throws Exception When simulation running.
     */
    public void updateTraffic() throws Exception {

        if (!getSimulationState()) {
            for (Line line : this.lines) {
                line.resetTimetable();
            }
            for (Street street : this.streetMap.getStreets()) {
                if (street.getTrafficCoefficient() > 1) {
                    updateTablebyTraffic(street);
                }
            }
        } else {
            throw new Exception("Pre zmenu nastavení ulice je potrebné pozastaviť čas...");
        }


    }

    /**
     * Aktualizuje tabulku s novymi casmi stopiek (podla úrovne obmedzenia)
     * @param street.
     */
    private void updateTablebyTraffic(Street street) {
        List<Line> returnLines = new ArrayList<>();
        List<Stop> lineStops = new ArrayList<>();
        for (int i = 0; i < this.lines.size(); i++) {
            if (this.lines.get(i).getStreets().contains(street)) {
                returnLines.add(this.lines.get(i));
            }
        }

        for (Line line : returnLines) {
            lineStops = line.getRealStops();
            for (int i = 0; i < lineStops.size() - 1; i++) {
                List<Street> streetsBetween = line.getStreetsBetween(lineStops.get(i), lineStops.get(i + 1));
                if (streetsBetween.size() == 1) {
                    if (streetsBetween.get(0).equals(street)) {
                        double lenghtOfStreet = line.getLenghtOfStreet(street);
                        double lenghtOfStops = line.getStopsLength(line.getStopByIndex(i), line.getStopByIndex(i + 1));
                        for (int k = 0; k < line.getPaths().size(); k++) {
                            List<LocalTime> times = line.getPaths().get(k).getActualTimetable();
                            LocalTime first = times.get(i);
                            LocalTime second = times.get(i + 1);
                            int sumTime = minusLocalTime(second, first);
                            long trafficTime = Math.round((double) (lenghtOfStreet / lenghtOfStops * sumTime) * street.getTrafficCoefficient() - (lenghtOfStreet / lenghtOfStops * sumTime));
                            for (int t = i + 1; t < times.size(); t++) {
                                LocalTime newTime = times.get(t);
                                newTime = newTime.plusSeconds(trafficTime);
                                times.set(t, newTime);
                            }
                        }
                    }
                } else if (streetsBetween.contains(street)) {
                    for (int j = 0; j < streetsBetween.size(); j++) {
                        if (streetsBetween.get(j).equals(street)) {
                            if (lineStops.get(i).getStreet().equals(street)) {
                                Coordinate follow = line.followPoint(streetsBetween.get(j), streetsBetween.get(j + 1));
                                updateStopsTime(street, lineStops, line, i, follow);
                            } else if (lineStops.get(i + 1).getStreet().equals(street)) {
                                Coordinate follow = line.followPoint(streetsBetween.get(j - 1), streetsBetween.get(j));
                                updateStopsTime(street, lineStops, line, i, follow);
                            } else {
                                double lenghtOfStreet = line.getLenghtOfStreet(street);
                                double lenghtOfStops = line.getStopsLength(line.getStopByIndex(i), line.getStopByIndex(i + 1));
                                for (int k = 0; k < line.getPaths().size(); k++) {
                                    List<LocalTime> times = line.getPaths().get(k).getActualTimetable();
                                    LocalTime first = times.get(i);
                                    LocalTime second = times.get(i + 1);
                                    int sumTime = minusLocalTime(second, first);
                                    long trafficTime = Math.round((double) (lenghtOfStreet / lenghtOfStops * sumTime) * street.getTrafficCoefficient() - (lenghtOfStreet / lenghtOfStops * sumTime));
                                    for (int t = i + 1; t < times.size(); t++) {
                                        LocalTime newTime = times.get(t);
                                        newTime = newTime.plusSeconds(trafficTime);
                                        times.set(t, newTime);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Vypočita nové časy pre zastávky (napr. pri definovaní obmedzenia na trase).
     * @param street Ulica
     * @param lineStops Zoznam všetkých stopiek na ulici
     * @param line linka
     * @param i aktuálna súradnica
     * @param follow nasledujúca súradnica
     */
    private void updateStopsTime(Street street, List<Stop> lineStops, Line line, int i, Coordinate follows) {
        Stop tempStop = new Stop("temp", follows);
        tempStop.setStreet(street);
        double temp = line.getDistBetweenStops(lineStops.get(i), tempStop);
        double length = line.getDistBetweenStops(lineStops.get(i), lineStops.get(i + 1));
        for (int k = 0; k < line.getPaths().size(); k++) {
            List<LocalTime> times = line.getPaths().get(k).getActualTimetable();
            LocalTime first = times.get(i);
            LocalTime second = times.get(i + 1);
            int sumTime = minusLocalTime(second, first);
            long trafficTime = Math.round((double) (temp /length * sumTime) * street.getTrafficCoefficient() - (temp / length * sumTime));
            for (int t = i + 1; t < times.size(); t++) {
                LocalTime newTime = times.get(t);
                newTime = newTime.plusSeconds(trafficTime);
                times.set(t, newTime);
            }
        }
    }

    /**
     * Funkcia pre zistenie, či je na trase vozidla obmedzenie
     * @return ffff
     */
    public boolean isConflict()
    {
        for(Line line:this.lines){
            if(line.isConflict())
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Získa obmedzenia pre všetky linky na mape
     */
    public void setLinesBlock() {
        for (Line line : this.getLines()) {
            line.getAllConflicts();
        }
    }


    /**
     * Začne pohyb vozidiel
     */
    public void start() throws Exception {
        start(LocalTime.now());
    }

    /**
     * Spustí animáciu (volá sa cez StartButton)
     * @param time cas
     */
    public void start(LocalTime time) {
        if (!simulationState) {
            final TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (!simulationTask) {
                        simulationTask = true;
                        gui.showTime(simulationTime);
                        if (previousRealTime == null) {
                            positionsUpdate();
                            previousRealTime = LocalTime.now();
                            simulationTask = false;
                            positionsSpeed();
                            return;
                        }
                        LocalTime diffTime = LocalTime.now().minusHours(previousRealTime.getHour())
                                .minusMinutes(previousRealTime.getMinute())
                                .minusSeconds(previousRealTime.getSecond());

                        int realTime = (diffTime.getHour() * 60 * 60) + (diffTime.getMinute() * 60) + (diffTime.getSecond());
                        if (realTime >= updateSpeed) {
                            positionsUpdate();
                            previousRealTime = LocalTime.now();
                        }
                        positionsSpeed();
                        simulationTask = false;
                    }
                }
            };


            this.simulationTime = time;
            this.simulationState = true;
            this.timer = new Timer("Main Timer");
            timer.schedule(timerTask, 0, 1000);
            System.err.println("Spustené.");
        } else {
            System.err.println("Už je spustené...");
        }


    }

    /**
     * Nastaví rýchlosť simulácie.
     * @param moveUpdateSpeed rc
     */
    public void setMoveUpdateSpeed(double moveUpdateSpeed) throws Exception {
        if (this.simulationState) {
            stop();
            this.moveUpdateSpeed = (int) Math.round(1000 * moveUpdateSpeed);
            start(this.simulationTime);
        } else {
            this.moveUpdateSpeed = (int) Math.round(1000 * moveUpdateSpeed);
        }
    }


    /**
     * Nastaví obnovovanie GUI - minimum je jedna sekunda
     * @param updateSpeed rychlost casu
     * @throws Exception e
     */
    public void setGuiUpdateSpeed(int updateSpeed) throws Exception {
        if (updateSpeed < 1) {
            throw new Exception("Rýchlosť musi byť >1");
        }
        if (this.simulationState) {
            stop();
            this.updateSpeed = updateSpeed;
            start(this.simulationTime);
        } else {
            this.updateSpeed = updateSpeed;
        }
    }

    /**
     * Vráti stav simulovania (ide/nejde).
     * @return Stav bool
     */
    public boolean getSimulationState() {
        return this.simulationState;
    }

    /**
     * Zastaví simulovanie (ak je spustené)
     */
    public void stop() {
        if (this.simulationState) {
            this.simulationState = false;
            if (this.timer != null) {
                this.previousRealTime = null;
                this.timer.cancel();
                this.timer.purge();
                System.err.println("Simulácia zastavená");
            }
        }
    }

    /**
     * Nastaví čas simulácie na Čas získaný z textfield-u
     * @param time t
     */
    public void setSimulationTime(String time) throws Exception {
        if (!simulationState) {
            simulationTime = LocalTime.parse(time);
        } else {
            throw new Exception("Pre zmenu rýchlosti zastavte simuláciu...");
        }
    }

}