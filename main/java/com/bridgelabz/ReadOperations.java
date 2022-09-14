package com.bridgelabz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class ReadOperations {
    /**
     * Purpose : Count the number of records Check the file name is correct Check
     * the file type is correct Return a Customized Exception if false
     *
     * @param filePathRead : to read the file path
     * @param fileNameUser : to read the file name
     */
    public int readDataCount(String filePathRead, String fileNameUser) throws StateSensorAnalyserException {
        final AtomicBoolean firstLine = new AtomicBoolean(true);
        final AtomicInteger count = new AtomicInteger();

        try {
            File file = new File(filePathRead);
            String fileName = file.getName();
            String fileNameWithoutExtension = " ";
            int pos = fileName.lastIndexOf(".");
            if (pos > 0 && pos < (fileName.length() - 1)) {
                fileNameWithoutExtension = fileName.substring(0, pos);
            }

            if (!fileNameWithoutExtension.equals(fileNameUser)) {
                throw new StateSensorAnalyserException("Please enter a proper file name!",
                        StateSensorAnalyserException.Message.IMPROPER_FILE_NAME);
            }

            if (!fileName.contains(".csv"))
                throw new StateSensorAnalyserException("Please enter a proper file type!",
                        StateSensorAnalyserException.Message.IMPROPER_FILE_TYPE);

            Files.lines(Paths.get(filePathRead)).forEach(lines -> {
                if (lines.startsWith("State"))
                    firstLine.set(false);
                else {
                    lines.split("\n");
                    count.getAndIncrement();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(String.valueOf(count));
    }

    /**
     * Purpose : Check whether the delimiter entered in correct or not If false,
     * return Customized Exception
     *
     * @param filePath  : to read the file path
     * @param delimiter : to read the delimiter
     */
    public boolean readDelimiter(String filePath, String delimiter) throws IOException {
        Scanner input = new Scanner(Paths.get(filePath));
        boolean flag = true;
        try {
            input.useDelimiter(",");
            Pattern result = input.delimiter();

            if (result.pattern().equals(delimiter))
                flag = true;
            else {
                flag = false;
                throw new StateSensorAnalyserException("Please enter a correct delimiter!",
                        StateSensorAnalyserException.Message.IMPROPER_DELIMITER);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * Purpose : Check whether the header matches correctly or not If false, return
     * Customized Exception
     *
     * @param filePath   : to read the file path
     * @param stringName : to read the header of the resource file
     */
    public boolean readHeader(String filePath, List<String> stringName)
            throws IOException, StateSensorAnalyserException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        // Reading header
        String line = br.readLine();
        String[] header = line.split(",");
        boolean flag = true;
        for (int i = 0; i < header.length && i < stringName.size(); i++) {
            if (stringName.get(i).equals(header[i]))
                flag = true;
            else {
                flag = false;
                throw new StateSensorAnalyserException("Please enter the correct header!",
                        StateSensorAnalyserException.Message.IMPROPER_HEADER);
            }
        }
        return flag;
    }
}