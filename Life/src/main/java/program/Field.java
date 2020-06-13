package program;

import com.sun.javaws.exceptions.InvalidArgumentException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;


public class Field extends JComponent {

    private Config config;
    private boolean reconfigure;
    private BufferedImage image;
    private BufferedImage impactImage;

    public boolean changeFieldByMouse = true;


    private List<List<Cell>> cells;

    private boolean showImpact = false;

    private JScrollPane repaintScrollWithChange;

    public void setMainFrame(JScrollPane component) {
        this.repaintScrollWithChange = component;
    }

    Field(Config config) {
        this.config = config;
        reconfigure = true;
        cells = new ArrayList<>();
        setBackground(Color.GREEN);

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (handleMouseAction(e.getX(),e.getY()) && changeFieldByMouse) {
                    repaint();
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (handleMouseAction(e.getX(),e.getY()) && changeFieldByMouse) {
                    repaint();
                }
            }
        });
    }


    @Override
    public void paintComponent(Graphics g) {

        if (reconfigure) {
            if (cells.size() < config.rows) {
                for (int i = cells.size(); i < config.rows; i++) {
                    cells.add(new ArrayList<>((i % 2 == 0) ? config.columns : config.columns - 1));
                }
            } else if (cells.size() > config.rows) {
                for (int i = cells.size() - 1; i >= config.rows; i--) {
                    cells.remove(i);
                    config.removeRow(i);
                }
            }

            for (int i = 0; i < cells.size(); i++) {
                for (int j = cells.get(i).size(); (i % 2 == 0 && j < config.columns) || (i % 2 == 1 && j < config.columns - 1); j++) {
                    cells.get(i).add(new Cell());
                }
            }
            for (int i = 0; i < cells.size(); i++) {
                for (int j = cells.get(i).size() - 1; (i % 2 == 0 && j >= config.columns) || (i % 2 == 1 && j >= config.columns - 1); j--) {
                    cells.get(i).remove(j);
                }
            }

            for (int i = cells.get(0).size() - 1; i >= config.columns; i--) {
                config.removeColumn(i);
            }

            for (List<Cell> l : cells) {
                for (Cell cell : l) {
                    cell.setAlive(false);
                }
            }

            for (Point p : config.coloredCells) {
                cells.get(p.y).get(p.x).setAlive(true);
            }


            int x = config.getInPixelsFieldWidth() + 25;
            int y = config.getInPixelsFieldHeight() + 25;
            image = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
            impactImage = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);

            Graphics2D imageG = image.createGraphics();


            imageG.setBackground(Color.WHITE);
            imageG.clearRect(0, 0, image.getWidth(), image.getHeight());

            imageG.setStroke(new BasicStroke(config.boundWidth));


            int startingPositionX = 5 + config.boundWidth / 2 + config.getInPixelsInnerRadius();
            int startingPositionY = 5 + config.boundWidth / 2 + config.getInPixelsOuterRadius();


            imageG.setColor(Color.BLACK);
            for (int i = 0; i < cells.size(); i += 2) {
                for (int j = 0; j < cells.get(i).size(); j++) {
                    cells.get(i).get(j).setCenterPosition(
                            startingPositionX + j * (2 * config.getInPixelsInnerRadius()),
                            startingPositionY + i / 2 * (3 * config.getInPixelsOuterRadius() - 1));
                    cells.get(i).get(j).setOffsets(config.getInPixelsInnerRadius(), config.getInPixelsOuterRadius());
                    cells.get(i).get(j).paintBounds(imageG);
                }
            }

            startingPositionX += config.getInPixelsInnerRadius();
            startingPositionY += 3 * config.getInPixelsOuterRadius() / 2;

            for (int i = 1; i < cells.size(); i += 2) {
                for (int j = 0; j < cells.get(i).size(); j++) {
                    cells.get(i).get(j).setCenterPosition(
                            startingPositionX + j * (2 * config.getInPixelsInnerRadius()),
                            startingPositionY + (i / 2) * (3 * config.getInPixelsOuterRadius() - 1));
                    cells.get(i).get(j).setOffsets(config.getInPixelsInnerRadius(), config.getInPixelsOuterRadius());
                    cells.get(i).get(j).paintBounds(imageG);
                }
            }

            reconfigure = false;
        }


        Graphics2D imageG = impactImage.createGraphics();
        imageG.setBackground(Color.WHITE);
        imageG.clearRect(0, 0, impactImage.getWidth(), impactImage.getHeight());

        calculateImpacts();
        for (List<Cell> c1 : cells) {
            for (Cell cell : c1) {
                cell.drawImpact(imageG);
            }
        }

        for (List<Cell> c1 : cells) {
            for (Cell cell : c1) {
                if (cell.isAlive())
                    floodFill(image.createGraphics(), cell.getCenterPosition(), Color.GREEN);
                else
                    floodFill(image.createGraphics(), cell.getCenterPosition(), Color.WHITE);
            }
        }

        g.setXORMode(Color.WHITE);
        g.drawImage(image, 0, 0, this);
        if (showImpact && config.edgeLength > 12)
            g.drawImage(impactImage, 0, 0, this);
    }

    public void setNewConfig(Config config) {
        if (!(0 <= config.liveBegin
                && config.liveBegin <= config.birthBegin
                && config.birthBegin <= config.birthEnd
                && config.birthEnd <= config.liveEnd))
            throw new IllegalArgumentException();

        this.config = config;
        reconfigure = true;
        repaint();
        repaintScrollWithChange.revalidate();
    }


    public Config getConfig() {
        return config;
    }


    /**
     * needed for scrolling component
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(config.getInPixelsFieldWidth() + 20, config.getInPixelsFieldHeight() + 20);
    }


    private Cell lastChanged;
    private long lastChangedTime;

    /**
     * @return true if it changed cells state
     */
    private boolean handleMouseAction(int x, int y) {
        if (x >= image.getWidth() || y >= image.getHeight()
                || x < 0 || y < 0)
            return false;

        Color c = new Color(image.getRGB(x, y));

        if (c.equals(Color.BLACK) || !checkIfCoordinateInTheCell(x, y, c))
            return false;

        int i;
        for (i = 0; i < cells.size(); i++) {
            Point center = cells.get(i).get(0).getCenterPosition();
            if (y < center.getY())
                break;
        }
        if (i == cells.size())
            i = cells.size() - 1;
        else if (i != 0 &&
                cells.get(i).get(0).getCenterPosition().getY() - y > y - cells.get(i-1).get(0).getCenterPosition().getY())
            i = i - 1;

        int j;
        for (j = 0; j < cells.get(i).size(); j++) {
            Point center = cells.get(i).get(j).getCenterPosition();
            if (x < center.getX())
                break;
        }
        if (j == cells.get(i).size())
            j = cells.get(i).size() - 1;
        else if (j != 0 &&
                cells.get(i).get(j).getCenterPosition().getX() - x > x - cells.get(i).get(j-1).getCenterPosition().getX())
            j = j - 1;


        if ((Instant.now().toEpochMilli() - lastChangedTime > 300))
            lastChanged = null;

        if (lastChanged != null && lastChanged.equals(cells.get(i).get(j)))
            return false;

        lastChanged = cells.get(i).get(j);
        lastChangedTime = Instant.now().toEpochMilli();
        if (!cells.get(i).get(j).isAlive()) {
            cells.get(i).get(j).setAlive(true);
            config.addCoordinates(j, i);;
        }
        else if (config.xorOption) {
            cells.get(i).get(j).setAlive(false);
            config.removeCoordinates(j, i);;
        }
        else {
            return false;
        }
        return true;
    }

    private boolean checkIfCoordinateInTheCell(int x, int y, Color color) {
        try {
            int delta = 1;
            while (image.getRGB(x, y + delta) == color.getRGB()) {
                delta++;
            }
            delta = 1;
            while (image.getRGB(x, y - delta) == color.getRGB()) {
                delta++;
            }
            delta = 1;
            while (image.getRGB(x + delta, y) == color.getRGB()) {
                delta++;
            }
            delta = 1;
            while (image.getRGB(x - delta, y) == color.getRGB()) {
                delta++;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    private void floodFill(Graphics2D g, Point firstPoint, Color newColor) {
        int oldRGB = image.getRGB(firstPoint.x, firstPoint.y);

        if (newColor.equals(new Color(oldRGB)))
            return;
        g.setColor(newColor);


        List<Point> stack = new ArrayList<>();
        stack.add(firstPoint);
        while (stack.size() != 0) {
            Point point = stack.get(stack.size() - 1);
            stack.remove(stack.size() - 1);

            int yCur = point.y;
            while (yCur >= 1 && image.getRGB(point.x, yCur) == oldRGB)
                yCur--;
            yCur += 1;
            boolean spanLeft = false;
            boolean spanRight = false;
            while (yCur < image.getHeight() && image.getRGB(point.x, yCur) == oldRGB) {
                g.drawLine(point.x, yCur, point.x, yCur);

                if (!spanLeft && point.x > 1 && image.getRGB(point.x - 1, yCur) == oldRGB) {
                    stack.add(new Point(point.x - 1, yCur));
                    spanLeft = true;
                } else if (spanLeft && point.x > 1 && image.getRGB(point.x - 1, yCur) != oldRGB) {
                    spanLeft = false;
                }

                if (!spanRight && point.x < image.getWidth()-1 && image.getRGB(point.x + 1, yCur) == oldRGB) {
                    stack.add(new Point(point.x + 1, yCur));
                    spanRight = true;
                } else if (spanRight && point.x < image.getWidth() && image.getRGB(point.x + 1, yCur) != oldRGB) {
                    spanRight = false;
                }

                yCur++;
            }

        }
    }

    public void restart() {
        config.coloredCells.clear();
        for (List<Cell> l : cells) {
            for (Cell cell : l) {
                cell.setAlive(false);
            }
        }
        repaint();
    }

    private void calculateImpacts() {
        BiFunction<Integer, Integer, Double> f = (y, x) -> {
          try {
              if (cells.get(y).get(x).isAlive())
                  return 1.0;
          } catch (IndexOutOfBoundsException e) {
          }
          return 0.0;
        };

        for(int i = 0 ; i < cells.size(); i++) {
            for(int j = 0; j < cells.get(i).size(); j ++) {
                int corrector = 0;
                if (i % 2 == 1)
                    corrector = 1;

                double first = 0;
                first += f.apply(i,j-1);
                first += f.apply(i-1,j-1 + corrector);
                first += f.apply(i-1,j + corrector);
                first += f.apply(i,j+1);
                first += f.apply(i+1,j + corrector);
                first += f.apply(i+1,j-1 + corrector);
                first *= config.firstImpact;

                double second = 0;
                second += f.apply(i-1,j-2 + corrector);
                second += f.apply(i-2,j);
                second += f.apply(i-1,j+1 + corrector);
                second += f.apply(i+1,j+1 + corrector);
                second += f.apply(i+2,j);
                second += f.apply(i+1,j-2 + corrector);
                second *= config.secondImpact;

                cells.get(i).get(j).setImpact(first + second);
            }
        }
    }

    public void nextStep() {

        for(int i = 0 ; i < cells.size(); i++) {
            for (int j = 0; j < cells.get(i).size(); j++) {
                Cell cell = cells.get(i).get(j);
                double im = cell.getImpact();
                if (!cell.isAlive() && (config.birthBegin <= im && im <= config.birthEnd)) {
                    cell.setAlive(true);
                    config.addCoordinates(j, i);
                } else if (cell.isAlive() && (im < config.liveBegin || im > config.liveEnd)) {
                    cell.setAlive(false);
                    config.removeCoordinates(j, i);
                }
            }
        }
        repaint();
    }

    public void changeImpactShowing() {
        showImpact = !showImpact;
        repaint();
    }
}
