package com.telematica.travelmate.modules;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
  Provides bus
 */

@Module
public class BusModule {

    @Provides
    @Singleton
    public Bus provideBus() {
        return new Bus();
    }
}
