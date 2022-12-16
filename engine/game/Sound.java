package engine.game;

import javax.sound.sampled.*;
import java.io.*;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;

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

    /**
     * Get a clip with echo effect
     * @param tag Original sound tag
     * @param delayInMilliSeconds delay between each echo
     * @param decayFactor how much the echo will decay each time it hits any objects
     * @param echoTime how many echos there will be
     * @return the clip with echo effect
     */
    public static Clip getEchoClip(String tag, int delayInMilliSeconds, float decayFactor, int echoTime) {
        AudioInputStream stream = getAudioInputStream(tag);
        AudioFormat audioFormat = stream.getFormat();

        // Transfer sound samples to floats
        byte[] byteArray = stream2bytes(stream);
        float[] samples = bytes2floats(byteArray, audioFormat);

        // Delay samples for each echo
        int delaySamples = (int) (delayInMilliSeconds * (audioFormat.getSampleRate() / 1000));

        // Get each echo
        float[][] echoSamples = new float[echoTime][];
        float[] lastSample = samples;
        int finalFloatLength = samples.length;
        for(int i = 0; i < echoTime; i++) {
            float[] echoSample = decayFilter(lastSample, decayFactor);
            lastSample = echoSample;
            echoSamples[i] = echoSample;
            finalFloatLength += echoSample.length;
        }

        // Combine(concatenate) each sound
        float[] combinedAudioSamples = new float[finalFloatLength + delaySamples * echoTime];
        int pos = 0;
        for (float sample : samples) {
            combinedAudioSamples[pos] = sample;
            pos++;
        }
        for(int i = 0; i < echoTime; i++) {
            pos += delaySamples;
            for(int j = 0; j < echoSamples[i].length; j++) {
                combinedAudioSamples[pos] = echoSamples[i][j];
                pos++;
            }
        }

        return floats2clip(combinedAudioSamples, audioFormat);
    }

    /**
     * Get a clip with reverb effect
     * @param tag Original sound tag
     * @param delayInMilliSeconds delay of the reverb from the start time
     * @param decayFactor how much the reverb will decay
     * @return the clip with reverb effect
     */
    public static Clip getReverbClip(String tag, int delayInMilliSeconds, float decayFactor) {
        int reverbTime = 4;

        AudioInputStream stream = getAudioInputStream(tag);
        AudioFormat audioFormat = stream.getFormat();

        // Transfer sound samples to floats
        byte[] byteArray = stream2bytes(stream);
        float[] samples = bytes2floats(byteArray, audioFormat);

        // Delay samples for each echo
        int delaySamples = (int) (delayInMilliSeconds * (audioFormat.getSampleRate() / 1000));

        // Calculate reverb samples for each reverb
        float[][] reverbSamples = new float[reverbTime][];
        float[] decayFactors = {decayFactor, decayFactor - 0.1313f, decayFactor - 0.2743f, decayFactor - 0.31f};
        for(int i = 0; i < reverbTime; i++) {
            float[] reverbSample = decayFilter(samples, decayFactors[i]);
            reverbSamples[i] = reverbSample;
        }

        // Calculate final length of the sound
        float[] delayTimes = {delayInMilliSeconds, delayInMilliSeconds - 11.73f, delayInMilliSeconds + 19.31f, delayInMilliSeconds + 7.97f};
        int maxLength = samples.length;
        for(int i = 0; i < delayTimes.length; i++) {
            maxLength = Math.max(maxLength, (int)(reverbSamples[i].length + delayTimes[i] * delaySamples) + 1);
        }

        // Combine all samples into one
        float[] combinedAudioSamples = new float[maxLength];
        System.arraycopy(samples, 0, combinedAudioSamples, 0, samples.length);
        for(int i = 0; i < reverbTime; i++) {
            for(int j = 0; j < reverbSamples[i].length; j++) {
                combinedAudioSamples[(int)(j + delayTimes[i] * delaySamples)] += reverbSamples[i][j];
            }
        }

        return floats2clip(combinedAudioSamples, audioFormat);
    }

    /**
     * Decay the samples with decayFactor
     * @param samples Original sound
     * @param decayFactor decayFactor
     * @return new samples after decaying
     */
    public static float[] decayFilter(float[] samples, float decayFactor) {
        float[] filteredSamples = new float[samples.length];

        for (int i = 0; i < samples.length; i++)
            filteredSamples[i] = (samples[i] * decayFactor);  // Lower down the volume

        return filteredSamples;
    }

    /**
     * Transfer the float-represented samples to clip
     * @param combinedAudioSamples float-represented samples
     * @param audioFormat format of the original audio
     * @return the clip
     */
    private static Clip floats2clip(float[] combinedAudioSamples, AudioFormat audioFormat) {
        byte[] finalAudioSamples = floats2bytes(combinedAudioSamples, audioFormat);

        ByteArrayInputStream stream = new ByteArrayInputStream(finalAudioSamples);
        AudioInputStream outputAis = new AudioInputStream(stream, audioFormat, finalAudioSamples.length);

        Clip clip;
        try {
            clip = AudioSystem.getClip();
            clip.open(outputAis);
        } catch (LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }

        return clip;
    }

    /**
     * Transfer the stream to bytes
     * @param stream the original stream
     * @return a bytes array
     */
    private static byte[] stream2bytes(AudioInputStream stream) {
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();

        try {
            AudioSystem.write(stream, AudioFileFormat.Type.WAVE, byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Transfer the bytes-represented samples to float-represented
     * For we need to manipulate the sound and samples are actually represented by multiple bytes
     * We need to transfer the bytes array to float arrays, to make sure that each float represent a single sample
     * @param bytes original bytes-represented samples
     * @param audioFormat format of the audio
     * @return a float array
     */
    public static float[] bytes2floats(byte[] bytes, AudioFormat audioFormat) {
        int bitsPerSample = audioFormat.getSampleSizeInBits();
        int bytesPerSample = bitsPerSample / 8;

        float[] samples;
        if(bytes.length % bytesPerSample == 0)
            samples = new float[bytes.length / bytesPerSample];
        else
            samples = new float[bytes.length / bytesPerSample + 1];

        for(int startIndex = 0; startIndex < bytes.length; startIndex += bytesPerSample)
            samples[startIndex / bytesPerSample] = bytes2float(bytes, startIndex, bytesPerSample);

        return samples;
    }

    /**
     * Transfer the float-represented samples to bytes-represented
     * @param samples float-represented samples
     * @param audioFormat format of the audio
     * @return a bytes array
     */
    public static byte[] floats2bytes(float[] samples, AudioFormat audioFormat) {
        int bitsPerSample = audioFormat.getSampleSizeInBits();
        int bytesPerSample = bitsPerSample / 8;

        byte[] bytes = new byte[bytesPerSample * samples.length];

        for(int startIndex = 0; startIndex < samples.length; startIndex++)
            float2bytes(bytes, startIndex * bytesPerSample, samples[startIndex], bytesPerSample);

        return bytes;
    }

    /**
     * Transfer a sample to float
     * @param bytes the original bytes array
     * @param i where the sample start
     * @param bytesPerSample bytesPerSample
     * @return
     */
    private static float bytes2float(byte[] bytes, int i, int bytesPerSample) {
        int res = 0;
        if(bytesPerSample == 1) {
            res += bytes[i];
        } else if(bytesPerSample == 2) {
            if(i + 1 < bytes.length) res += bytes[i + 1];
            res = res << 8;
            res += bytes[i];
        } else if(bytesPerSample == 3) {
            if(i + 2 < bytes.length) res += bytes[i + 2];
            res += res << 8;
            if(i + 1 < bytes.length) res += bytes[i + 1];
            res = res << 8;
            res += bytes[i];
        }
        return res;
    }

    /**
     * Transfer a sample to bytes
     * @param bytes the original bytes array
     * @param i where the sample start
     * @param sample the sample in float
     * @param bytesPerSample bytesPerSample
     */
    private static void float2bytes(byte[] bytes, int i, float sample, int bytesPerSample) {
        long temp = (long) sample;
        if(bytesPerSample == 1) {
            bytes[i] = (byte)(temp & 0b11111111);
        } else if(bytesPerSample == 2) {
            bytes[i] = (byte)(temp & 0b11111111);
            bytes[i + 1] = (byte)((temp >> 8) & 0b11111111);
        } else if(bytesPerSample == 3) {
            if(i + 2 < bytes.length) bytes[i + 2] = (byte)(temp & 0b11111111);
            if(i + 1 < bytes.length) bytes[i + 1] = (byte)((temp >> 8) & 0b11111111);
            bytes[i] = (byte)((temp >> 16) & 0b11111111);
        }
    }
}
