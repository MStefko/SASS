/* 
 * Copyright (C) 2017 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Federale de Lausanne
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.epfl.leb.sass.simulator.generators.realtime;

/**
 * Represents the parameters of the camera.
 * 
 * @author Marcel Stefko
 * @author Kyle M. Douglass
 * @deprecated Use Camera class from the components package instead.
 */
@Deprecated
public final class Camera {

    /**
     * frame rate [frames/second]
     */
    public final int acq_speed;

    /**
     * readout noise of camera [RMS]
     */
    public final double readout_noise;

    /**
     * dark current [electrons/second/pixel]
     */
    public final double dark_current;

    /**
     * quantum efficiency [0.0-1.0]
     */
    public final double quantum_efficiency;

    /**
     * Conversion factor between camera's analog-to-digital units (ADU) and electrons. [-]
     */
    public final double ADU_per_electron;
    
    /**
     * Electron multiplication (EM) gain of the camera.
     * This may be set to zero for sensors without EM gain, such as CMOS
     * sensors.
     */
    public final int EM_gain;

    /**
     * Camera pixel baseline (zero signal mean) [ADU]
     */
    public final int baseline;
    
    /**
     * physical size of pixel [m]
     */
    public final double pixel_size;

    /**
     * numerical aperture [-]
     */
    @Deprecated
    public final double NA;

    /**
     * light wavelength [m]
     */
    @Deprecated
    public final double wavelength;

    /**
     * magnification of camera [-]
     */
    @Deprecated
    public final double magnification;
    
    /**
     * noise in frame caused by dark current [electrons/frame/pixel]
     */
    public final double thermal_noise;

    /**
     * digital representation of the FWHM
     */
    @Deprecated
    public final double fwhm_digital;
    
    /**
     * horizontal image size [pixels]
     */
    public final int res_x;

    /**
     * vertical image size [pixels]
     */
    public final int res_y;
    
    /**
     * Displacement of the coverslip surface from the objective's focal plane.
     * 
     * This value assumes that the microscope objective is inverted (facing up)
     * and that the axial direction is positive going upwards. A negative value
     * therefore implies that the coverslip has been moved down towards the
     * objective bringing objects located above the coverslip into focus.
     */
    public double stagePosition = 0;
    
    public static class Builder {
        private int acqSpeed;
        private double readoutNoise;
        private double darkCurrent;
        private double quantumEfficiency;
        private double aduPerElectron;
        private int emGain;
        private int baseline;
        private double pixelSize;
        private double thermalNoise;
        private int nX;
        private int nY;
        
        public Builder acqSpeed(int acqSpeed) {
            this.acqSpeed = acqSpeed;
            return this;
        }
        public Builder readoutNoise(double readoutNoise) {
            this.readoutNoise = readoutNoise;
            return this;
        }
        public Builder darkCurrent(double darkCurrent) {
            this.darkCurrent = darkCurrent;
            return this;
        }
        public Builder quantumEfficiency(double quantumEfficiency) {
            this.quantumEfficiency = quantumEfficiency;
            return this;
        }
        public Builder aduPerElectron(double aduPerElectron) {
            this.aduPerElectron = aduPerElectron;
            return this;
        }
        public Builder emGain(int emGain) {
            this.emGain = emGain;
            return this;
        }
        public Builder baseline(int baseline) {
            this.baseline = baseline;
            return this;
        }
        public Builder pixelSize(double pixelSize) {
            this.pixelSize = pixelSize;
            return this;
        }
        public Builder thermalNoise(double thermalNoise) {
            this.thermalNoise = thermalNoise;
            return this;
        }
        public Builder nX(int nX) { this.nX = nX; return this; }
        public Builder nY(int nY) { this.nY = nY; return this; }
        
        public Camera build() {
            return new Camera(this);
        }
    }
    
    /**
     * Initialize camera with parameters.
     * @param res_x horizontal resolution [pixels]
     * @param res_y vertical resolution [pixels]
     * @param acq_speed frame rate [frames/second]
     * @param readout_noise readout noise of camera [RMS]
     * @param dark_current dark current [electrons/second/pixel]
     * @param quantum_efficiency quantum efficiency [0.0-1.0]
     * @param ADU_per_electron conversion between camera units and electrons [-]
     * @param EM_gain electron multiplication gain [-]
     * @param baseline zero-signal average of pixel values [ADU]
     * @param pixel_size physical size of pixel [m]
     * @param NA numerical aperture [-]
     * @param wavelength light wavelength [m]
     * @param magnification magnification of camera [-]
     * @deprecated Use {@link #Camera(ch.epfl.leb.sass.simulator.generators.realtime.Camera.Builder) } instead.
     */
    @Deprecated
    public Camera(int res_x, int res_y, int acq_speed, double readout_noise,
            double dark_current, double quantum_efficiency,
            double ADU_per_electron, int EM_gain, int baseline,
            double pixel_size, double NA, double wavelength,
            double magnification) {
        this.res_x = res_x;
        this.res_y = res_y;
        this.acq_speed = acq_speed;
        this.readout_noise = readout_noise;
        this.dark_current = dark_current;
        this.quantum_efficiency = quantum_efficiency;
        this.ADU_per_electron = ADU_per_electron;
        this.EM_gain = EM_gain;
        this.baseline = baseline;
        this.pixel_size = pixel_size;
        this.NA = NA;
        this.wavelength = wavelength;
        this.magnification = magnification;
        
        this.thermal_noise = this.dark_current / this.acq_speed;
        
        // calculate Gaussian PSF
        double airy_psf_radius = 0.61*wavelength/NA;
        double airy_psf_radius_digital = airy_psf_radius * magnification;

        fwhm_digital = airy_psf_radius_digital / pixel_size;
    }
    
    private Camera(Builder builder) {
        this.ADU_per_electron = builder.aduPerElectron;
        this.EM_gain = builder.emGain;
        this.acq_speed = builder.acqSpeed;
        this.baseline = builder.baseline;
        this.dark_current = builder.darkCurrent;
        this.pixel_size = builder.pixelSize;
        this.quantum_efficiency = builder.quantumEfficiency;
        this.readout_noise = builder.readoutNoise;
        this.thermal_noise = builder.thermalNoise;
        this.res_x = builder.nX;
        this.res_y = builder.nY;
        
        // Deprecated fields
        this.NA = 1.4;
        this.fwhm_digital = 3;
        this.magnification = 60;
        this.wavelength = 0.65;
    }
    
    /** 
     * @deprecated Use {@link #getNX() } instead.
     */
    @Deprecated
    public int getRes_X() {
        return this.res_x;
    }
    
    /**
     * @deprecated Use {@link #getNY() } instead.
     */
    @Deprecated
    public int getRes_Y() {
        return this.res_y;
    }
    
    public double getAduPerElectron() { return this.ADU_per_electron; }
    public int getAcqSpeed() { return this.acq_speed; }
    public int getBaseline() { return this.baseline; }
    public double getDarkCurrent() { return this.dark_current; }
    public double getPixelSize() { return this.pixel_size; }
    public double getQuantumEfficiency() { return this.quantum_efficiency; }
    public double getReadoutNoise() { return this.readout_noise; }
    public double getThermalNoise() { return this.thermal_noise; }
    
    /**
     * @return The number of pixels in x.
     */
    public int getNX() {
        return this.res_x;
    }
    
    /**
     * @return The number of pixels in y.
     */
    public int getNY() {
        return this.res_y;
    }
}
