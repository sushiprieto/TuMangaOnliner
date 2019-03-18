package ar.rulosoft.mimanganu;

import android.content.Context;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import ar.rulosoft.mimanganu.servers.ServerBase;
import ar.rulosoft.navegadores.Navigator;
import util.TestServersCommon;

@Config(
        shadows = {ShadowNavigator.class}
)
@RunWith(RobolectricTestRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestServers {

    private Context context;

    @Before
    public void setupTest() throws Exception {
        context = RuntimeEnvironment.application.getApplicationContext();
        Navigator.initialiseInstance(context);
    }

    @Ignore("FromFolder cannot be tested - yet")
    public void test_FROMFOLDER() throws Exception {
        new TestServersCommon(ServerBase.FROMFOLDER, true, context);
    }

    @Test
    public void test_TUSMANGAS() throws Exception {
        new TestServersCommon(ServerBase.TUSMANGAS, true, context);
    }

    @Ignore("Cannot be tested on host due to Duktape usage (needs JNI) - use instrumented tests")
    public void test_TUMANGAONLINE() throws Exception {
        new TestServersCommon(ServerBase.TUMANGAONLINE, true, context);
    }

}
