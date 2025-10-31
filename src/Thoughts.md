
<!-- File Structure --> 
How i will be arranging my files:




<!-- Adding JPANELS To JFRAMES -->
    NORTH  (top)
    SOUTH  (bottom)
    EAST   (right)
    WEST   (left)
    CENTER (middle)
    
    Jfram using grid Layout 
    // new JFrame(new GridLayout(2, 1)); <- 2 rows 1 col

<!-- Wrapping my head around GridLayout -->
=> Wrapping my head around GridLayout  

        JFrame frame = new JFrame("Project Skeleton Generator");
        frame.setSize(1080,1080);
        frame.setLayout(new GridLayout(2,1));


        JPanel UpperHalf = new JPanel();
        JPanel LowerHalf = new JPanel();
        JPanel LowerRight = new JPanel();
        JPanel LowerLeft = new JPanel();

        UpperHalf.setBackground(Color.red);
        LowerRight.setBackground(Color.green);
        LowerLeft.setBackground(Color.CYAN);
        LowerHalf.setLayout(new GridLayout(1,2));
        LowerHalf.add(LowerRight);
        LowerHalf.add(LowerLeft);
        
        frame.add(UpperHalf);
        frame.add(LowerHalf);
        frame.setVisible(true);


<!-- Creating the class rectangle --> 
=> Creating the class rectangle 

    (0,0) ------------------------------> X
    |
    |
    |           (x=100,y=100)
    |                ┌─────────────────────────────┐
    |                │                             │
    |                │        RECTANGLE            │
    |                │   width = 40, height = 40   │
    |                └─────────────────────────────┘
    ↓
    Y
    
    public static class DrawRect  extends JPanel {
        private static final int RECT_X = 0;
        private static final int RECT_Y = 0;
        private static final int RECT_WIDTH = 200;
        private static final int RECT_HEIGHT = 200;

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.lightGray);
            g.fillRect(RECT_X, RECT_Y, RECT_WIDTH, RECT_HEIGHT);

            g.setColor(Color.white);
            g.drawRect(RECT_X, RECT_Y, RECT_WIDTH, RECT_HEIGHT);
        }
    }


    then you add to the button actionListener, onMouseClick {
        you need to set the bounds of the rectangle inside the JPanel container
        you add the shape RECTANGLE to your JPanel
        after that you need to revalidate and repaint
    }

    => setting the bounds of the rectangle 
    



