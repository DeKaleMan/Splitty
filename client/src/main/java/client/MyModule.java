/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import client.scenes.*;
import client.utils.Config;
import client.utils.Mail;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

public class MyModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(InvitationCtrl.class).in(Scopes.SINGLETON);
        binder.bind(SplittyOverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(StartScreenCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AddExpenseCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ContactDetailsCtrl.class).in(Scopes.SINGLETON);
        binder.bind(Config.class).in(Scopes.SINGLETON);
        binder.bind(AdminLoginCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AdminOverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ContactDetailsCtrl.class).in(Scopes.SINGLETON);
        binder.bind(CreateEventCtrl.class).in(Scopes.SINGLETON);
        binder.bind(DebtCtrl.class).in(Scopes.SINGLETON);
        binder.bind(Config.class).in(Scopes.SINGLETON);
        binder.bind(ManageParticipantsCtrl.class).in(Scopes.SINGLETON);
        binder.bind(SettingsCtrl.class).in(Scopes.SINGLETON);
        binder.bind(StatisticsCtrl.class).in(Scopes.SINGLETON);
        binder.bind(UserEventListCtrl.class).in(Scopes.SINGLETON);
        binder.bind(EditEventCrtl.class).in(Scopes.SINGLETON);
        binder.bind(EditExpenseCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ServerCtrl.class).in(Scopes.SINGLETON);
        binder.bind(Mail.class).in(Scopes.SINGLETON);
    }
}