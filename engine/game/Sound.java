package engine.game;

import javax.sound.sampled.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;

public class Sound {
    private static HashMap<String, File> tag2Audio = new HashMap<>();

    public static void loadAudio(String path, String tag) {
        if(tag2Audio.containsKey(tag)) return;
        File file = new File(path);

        tag2Audio.put(tag, file);
    }

    private static AudioInputStream getAudioInputStream(String tag) {
        if(!tag2Audio.containsKey(tag)) {
            System.out.println("The audio " + tag + " does not exist!");
            return null;
        }
        InputStream in;
        try {
            in = new BufferedInputStream(new FileInputStream(tag2Audio.get(tag)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        AudioInputStream stream = null;
        try {
            stream = AudioSystem.getAudioInputStream(in);
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
        return stream;
    }

    public static Clip getAudio(String tag) {
        AudioInputStream stream = getAudioInputStream(tag);
        Clip clip;
        try {
            clip = AudioSystem.getClip();
            clip.open(stream);
        } catch (LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
        return clip;
    }

    public static Clip getEchoClip(String tag, int delayInMilliSeconds, float decayFactor, int echoTime) {
        AudioInputStream stream = getAudioInputStream(tag);

        AudioFormat audioFormat = stream.getFormat();
        float sampleRate = audioFormat.getSampleRate();
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();

        try {
            AudioSystem.write(stream, AudioFileFormat.Type.WAVE, byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] byteArray = byteArrayOutputStream.toByteArray();  // byte 乘了个4
        int bufferSize = byteArray.length;

        float[] samples = new float[bufferSize];
        unpack(byteArray, samples, byteArray.length, audioFormat);

        int delaySamples = (int) (delayInMilliSeconds * (sampleRate / 1000));

        float[][] echoSamples = new float[echoTime][];

        float[] lastSample = samples;
        int finalFloatLength = samples.length;
        for(int i = 0; i < echoTime; i++) {
            float[] echoSample = echoFilter(lastSample, bufferSize, decayFactor);
            lastSample = echoSample;
            echoSamples[i] = echoSample;
            finalFloatLength += echoSample.length;
        }

        float[] combinedAudioSamples = new float[finalFloatLength + delaySamples * echoTime];
        int pos = 0;
        for(int i = 0; i < samples.length; i++) {
            combinedAudioSamples[pos] = samples[i];
            pos++;
        }
        for(int i = 0; i < echoTime; i++) {
            pos += delaySamples;
            for(int j = 0; j < echoSamples[i].length; j++) {
                combinedAudioSamples[pos] = echoSamples[i][j];
                pos++;
            }
        }

        byte[] finalAudioSamples = pack(combinedAudioSamples, combinedAudioSamples.length, audioFormat);

        ByteArrayInputStream bais = new ByteArrayInputStream(finalAudioSamples);
        AudioInputStream outputAis = new AudioInputStream(bais, audioFormat,finalAudioSamples.length);

        Clip clip;
        try {
            clip = AudioSystem.getClip();
            clip.open(outputAis);
        } catch (LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
        return clip;
    }

    public static float[] echoFilter(float[] samples, int samplesLength, float decayFactor) {
        float[] echoFilterSamples = new float[samplesLength];

        for (int i = 0; i < samplesLength; i++)
        {
            echoFilterSamples[i] = (samples[i] * decayFactor);
        }
        return echoFilterSamples;
    }

    public static Clip getReverbClip(String tag, int delayInMilliSeconds, float decayFactor) {
        int reverbTime = 4;

        AudioInputStream stream = getAudioInputStream(tag);

        AudioFormat audioFormat = stream.getFormat();
        float sampleRate = audioFormat.getSampleRate();
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();

        try {
            AudioSystem.write(stream, AudioFileFormat.Type.WAVE, byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] byteArray = byteArrayOutputStream.toByteArray();  // byte 乘了个4
        int bufferSize = byteArray.length;

        float[] samples = new float[bufferSize];
        unpack(byteArray, samples, byteArray.length, audioFormat);

        int delaySamples = (int) (delayInMilliSeconds * (sampleRate / 1000));

        float[][] reverbSample = new float[reverbTime][];

        float[] decayFactors = {decayFactor, decayFactor - 0.1313f, decayFactor - 0.2743f, decayFactor - 0.31f};
        for(int i = 0; i < reverbTime; i++) {
            float[] echoSample = echoFilter(samples, bufferSize, decayFactors[i]);
            reverbSample[i] = echoSample;
        }


        float[] delayTimes = {delayInMilliSeconds, delayInMilliSeconds - 11.73f, delayInMilliSeconds + 19.31f, delayInMilliSeconds + 7.97f};
        int maxLength = samples.length;
        for(int i = 0; i < delayTimes.length; i++) {
            maxLength = Math.max(maxLength, (int)(reverbSample[i].length + delayTimes[i] * delaySamples) + 1);
        }
        float[] combinedAudioSamples = new float[maxLength];
        for(int i = 0; i < samples.length; i++) {
            combinedAudioSamples[i] = samples[i];
        }
        for(int i = 0; i < reverbTime; i++) {
            for(int j = 0; j < reverbSample[i].length; j++) {
                combinedAudioSamples[(int)(j + delayTimes[i] * delaySamples)] += reverbSample[i][j];
            }
        }

        byte[] finalAudioSamples = pack(combinedAudioSamples, combinedAudioSamples.length, audioFormat);

        ByteArrayInputStream bais = new ByteArrayInputStream(finalAudioSamples);
        AudioInputStream outputAis = new AudioInputStream(bais, audioFormat,finalAudioSamples.length);

        Clip clip;
        try {
            clip = AudioSystem.getClip();
            clip.open(outputAis);
        } catch (LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
        return clip;
    }

    public static int unpack(byte[] bytes, float[] samples, int byteLength, AudioFormat audioFormat) {
        int bitsPerSample = audioFormat.getSampleSizeInBits();
        int bytesPerSample = bitsPerSample / 8;
        Encoding encoding = audioFormat.getEncoding();
        double fullScale = fullScale(bitsPerSample);

        int i = 0;
        int s = 0;
        while (i < byteLength)
        {
            long temp = unpackBits(bytes, i, bytesPerSample);
            float sample = 0f;

            if (encoding == Encoding.PCM_SIGNED) {
                temp = extendSign(temp, bitsPerSample);
                sample = (float) (temp / fullScale);

            } else if (encoding == Encoding.PCM_UNSIGNED) {
                temp = signUnsigned(temp, bitsPerSample);
                sample = (float) (temp / fullScale);
            }
            samples[s] = sample;

            i += bytesPerSample;
            s++;
        }
        return s;
    }

    public static byte[] pack(float[] samples, int floatLength, AudioFormat audioFormat) {
        int   bitsPerSample = audioFormat.getSampleSizeInBits();
        int  bytesPerSample = bytesPerSample(bitsPerSample);
        Encoding   encoding = audioFormat.getEncoding();
        double    fullScale = fullScale(bitsPerSample);

        byte[] bytes = new byte[bytesPerSample * floatLength];

        int i = 0;
        int s = 0;
        while (s < floatLength) {
            float sample = samples[s];
            long temp = 0L;

            if (encoding == Encoding.PCM_SIGNED) {
                temp = (long) (sample * fullScale);

            } else if (encoding == Encoding.PCM_UNSIGNED) {
                temp = (long) (sample * fullScale);
                temp = unsignSigned(temp, bitsPerSample);
            }

            packBits(bytes, i, temp, bytesPerSample);

            i += bytesPerSample;
            s++;
        }
        return bytes;
    }

    private static long unpackBits(byte[] bytes, int i, int bytesPerSample) {
        long res;
        switch (bytesPerSample) {
            case 1:
                res = unpack8Bit(bytes, i);
                break;
            case 2:
                res = unpack16Bit(bytes, i);
                break;
            case 3:
                res = unpack24Bit(bytes, i);
                break;
            default:
                res = 1;
        };
        return res;
    }

    public static int bytesPerSample(int bitsPerSample) {
        return (int) Math.ceil(bitsPerSample / 8.0);
    }

    public static double fullScale(int bitsPerSample) {
        return Math.pow(2.0, bitsPerSample - 1);
    }

    private static long unpack8Bit(byte[] bytes, int i) {
        return bytes[i];
    }

    private static long unpack16Bit(byte[] bytes, int i) {
        long res = bytes[i];
        if(i + 1 < bytes.length) res |= (bytes[i + 1] << 8);
        return res;
    }

    private static long unpack24Bit(byte[] bytes, int i) {
        long res = bytes[i];
        if(i + 1 < bytes.length) res |= (bytes[i + 1] << 8);
        if(i + 2 < bytes.length) res |= (bytes[i + 2] << 16);
        return res;
    }

    public static long extendSign(long temp, int bitsPerSample) {
        int extensionBits = 64 - bitsPerSample;
        return (temp << extensionBits) >> extensionBits;
    }

    private static long signUnsigned(long temp, int bitsPerSample) {
        return temp - (long) fullScale(bitsPerSample);
    }

    private static long unsignSigned(long temp, int bitsPerSample) {
        return temp + (long) fullScale(bitsPerSample);
    }

    private static void packBits(byte[]  bytes,
                                 int     i,
                                 long    temp,
                                 int     bytesPerSample) {
        switch (bytesPerSample) {
            case 1:
                pack8Bit(bytes, i, temp);
                break;
            case 2:
                pack16Bit(bytes, i, temp);
                break;
            case 3:
                pack24Bit(bytes, i, temp);
                break;
            default: ;
                break;
        }
    }

    private static void pack8Bit(byte[] bytes, int i, long temp) {
        bytes[i] = (byte)temp;
    }

    private static void pack16Bit(byte[] bytes, int i, long temp)  {
        bytes[i] = (byte) temp;
        if(i + 1 < bytes.length) bytes[i + 1] = (byte) (temp >>> 8L);
    }

    private static void pack24Bit(byte[] bytes, int i, long temp)  {
        bytes[i] = (byte) temp;
        if(i + 1 < bytes.length) bytes[i + 1] = (byte) (temp >>> 8L);
        if(i + 2 < bytes.length) bytes[i + 2] = (byte) (temp >>> 16L);
    }

}
