import java.util.Random;

public class PageReplacement {

    public static void main(String[] args) {
        // Generate a random page reference string
        int[] pages = generatePages(10);
        for (int i = 0; i < pages.length; i++)
            System.out.print(pages[i] + " ");
        System.out.println();
        // Test the FIFO algorithm with different numbers of page frames
        for (int i = 1; i <= 7; i++) {
            int faults = fifo(pages, i);
            System.out.println("FIFO with " + i + " frames: " + faults + " page faults");
        }
        for (int i = 1; i <= 7; i++) {
            int faults = lru(pages, i);
            System.out.println("LRU with " + i + " frames: " + faults + " page faults");
        }
        for (int i = 1; i <= 7; i++) {
            int faults = opt(pages, i);
            System.out.println("OPT with " + i + " frames: " + faults + " page faults");
        }
    }

    // Generate a random page reference string
    public static int[] generatePages(int n) {
        int[] pages = new int[n];
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            pages[i] = rand.nextInt(10);
        }
        return pages;
    }

    // Implement the FIFO page replacement algorithm
    public static int fifo(int[] pages, int numFrames) {
        int[] frames = new int[numFrames];
        int faults = 0;
        int nextFrameIndex = 0;

        for (int i = 0; i < pages.length; i++) {
            boolean pageHit = false;
            int page = pages[i];

            // Check if the page is already in a frame
            for (int j = 0; j < numFrames; j++) {
                if (frames[j] == page) {
                    pageHit = true;
                    break;
                }
            }

            // If the page is not in a frame, add it to the next available frame
            if (!pageHit) {
                frames[nextFrameIndex] = page;
                nextFrameIndex = (nextFrameIndex + 1) % numFrames;
                faults++;
            }
        }

        return faults;
    }

    // Implement the LRU page replacement algorithm
    public static int lru(int[] pages, int numFrames) {
        int[] frames = new int[numFrames];
        int[] counters = new int[numFrames];
        int faults = 0;

        for (int i = 0; i < pages.length; i++) {
            boolean pageHit = false;
            int page = pages[i];

            // Check if the page is already in a frame
            for (int j = 0; j < numFrames; j++) {
                if (frames[j] == page) {
                    pageHit = true;
                    counters[j] = i;
                    break;
                }
            }

            // If the page is not in a frame, replace the least recently used page
            if (!pageHit) {
                int minCounter = counters[0];
                int minIndex = 0;
                for (int j = 1; j < numFrames; j++) {
                    if (counters[j] < minCounter) {
                        minCounter = counters[j];
                        minIndex = j;
                    }
                }
                frames[minIndex] = page;
                counters[minIndex] = i;
                faults++;
            }
        }

        return faults;
    }

    // Implement the OPT page replacement algorithm
    public static int opt(int[] pages, int numFrames) {
        int[] frames = new int[numFrames];
        int faults = 0;

        for (int i = 0; i < numFrames; i++) {
            frames[i] = -1; // initialize frames with -1
        }

        for (int i = 0; i < pages.length; i++) {
            boolean pageHit = false;
            int page = pages[i];

            // Check if the page is already in a frame
            for (int j = 0; j < numFrames; j++) {
                if (frames[j] == page) {
                    pageHit = true;
                    break;
                }
            }

            // If the page is not in a frame, find the page to be replaced
            if (!pageHit) {
                int index = -1;
                int farthest = i;
                for (int j = 0; j < numFrames; j++) {
                    int k;
                    for (k = i; k < pages.length; k++) {
                        if (frames[j] == pages[k]) {
                            if (k > farthest) {
                                farthest = k;
                                index = j;
                            }
                            break;
                        }
                    }
                    if (k == pages.length) {
                        index = j;
                        break;
                    }
                }
                frames[index] = page;
                faults++;
            }
        }

        return faults;
    }
}