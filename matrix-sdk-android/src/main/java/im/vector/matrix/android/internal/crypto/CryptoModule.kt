/*
 * Copyright 2019 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.matrix.android.internal.crypto

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import im.vector.matrix.android.api.auth.data.Credentials
import im.vector.matrix.android.api.session.crypto.CryptoService
import im.vector.matrix.android.internal.crypto.api.CryptoApi
import im.vector.matrix.android.internal.crypto.keysbackup.api.RoomKeysApi
import im.vector.matrix.android.internal.crypto.keysbackup.tasks.*
import im.vector.matrix.android.internal.crypto.store.IMXCryptoStore
import im.vector.matrix.android.internal.crypto.store.db.RealmCryptoStore
import im.vector.matrix.android.internal.crypto.store.db.RealmCryptoStoreMigration
import im.vector.matrix.android.internal.crypto.store.db.RealmCryptoStoreModule
import im.vector.matrix.android.internal.crypto.tasks.*
import im.vector.matrix.android.internal.database.configureEncryption
import im.vector.matrix.android.internal.di.CryptoDatabase
import im.vector.matrix.android.internal.session.SessionScope
import im.vector.matrix.android.internal.session.cache.ClearCacheTask
import im.vector.matrix.android.internal.session.cache.RealmClearCacheTask
import im.vector.matrix.android.internal.util.md5
import io.realm.RealmConfiguration
import retrofit2.Retrofit
import java.io.File

@Module
internal abstract class CryptoModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        @CryptoDatabase
        @SessionScope
        fun providesRealmConfiguration(context: Context, credentials: Credentials): RealmConfiguration {
            val userIDHash = credentials.userId.md5()

            return RealmConfiguration.Builder()
                    .directory(File(context.filesDir, userIDHash))
                    .configureEncryption("crypto_module_$userIDHash", context)
                    .name("crypto_store.realm")
                    .modules(RealmCryptoStoreModule())
                    .schemaVersion(RealmCryptoStoreMigration.CRYPTO_STORE_SCHEMA_VERSION)
                    .migration(RealmCryptoStoreMigration)
                    .build()
        }

        @JvmStatic
        @Provides
        @CryptoDatabase
        fun providesClearCacheTask(@CryptoDatabase
                                   realmConfiguration: RealmConfiguration): ClearCacheTask {
            return RealmClearCacheTask(realmConfiguration)
        }


        @JvmStatic
        @Provides
        fun providesCryptoStore(@CryptoDatabase
                                realmConfiguration: RealmConfiguration, credentials: Credentials): IMXCryptoStore {
            return RealmCryptoStore(
                    realmConfiguration,
                    credentials)
        }

        @JvmStatic
        @Provides
        @SessionScope
        fun providesCryptoAPI(retrofit: Retrofit): CryptoApi {
            return retrofit.create(CryptoApi::class.java)
        }

        @JvmStatic
        @Provides
        @SessionScope
        fun providesRoomKeysAPI(retrofit: Retrofit): RoomKeysApi {
            return retrofit.create(RoomKeysApi::class.java)
        }

        @JvmStatic
        @Provides
        @SessionScope
        fun providesCryptoConfig(): MXCryptoConfig {
            return MXCryptoConfig()
        }

    }

    @Binds
    abstract fun bindCryptoService(cryptoManager: CryptoManager): CryptoService

    @Binds
    abstract fun bindDeleteDeviceTask(deleteDeviceTask: DefaultDeleteDeviceTask): DeleteDeviceTask

    @Binds
    abstract fun bindGetDevicesTask(getDevicesTask: DefaultGetDevicesTask): GetDevicesTask

    @Binds
    abstract fun bindSetDeviceNameTask(getDevicesTask: DefaultSetDeviceNameTask): SetDeviceNameTask

    @Binds
    abstract fun bindUploadKeysTask(getDevicesTask: DefaultUploadKeysTask): UploadKeysTask

    @Binds
    abstract fun bindDownloadKeysForUsersTask(downloadKeysForUsers: DefaultDownloadKeysForUsers): DownloadKeysForUsersTask

    @Binds
    abstract fun bindCreateKeysBackupVersionTask(createKeysBackupVersionTask: DefaultCreateKeysBackupVersionTask): CreateKeysBackupVersionTask

    @Binds
    abstract fun bindDeleteBackupTask(deleteBackupTask: DefaultDeleteBackupTask): DeleteBackupTask

    @Binds
    abstract fun bindDeleteRoomSessionDataTask(deleteRoomSessionDataTask: DefaultDeleteRoomSessionDataTask): DeleteRoomSessionDataTask

    @Binds
    abstract fun bindDeleteRoomSessionsDataTask(deleteRoomSessionDataTask: DefaultDeleteRoomSessionsDataTask): DeleteRoomSessionsDataTask

    @Binds
    abstract fun bindDeleteSessionsDataTask(deleteRoomSessionDataTask: DefaultDeleteSessionsDataTask): DeleteSessionsDataTask

    @Binds
    abstract fun bindGetKeysBackupLastVersionTask(getKeysBackupLastVersionTask: DefaultGetKeysBackupLastVersionTask): GetKeysBackupLastVersionTask

    @Binds
    abstract fun bindGetKeysBackupVersionTask(getKeysBackupVersionTask: DefaultGetKeysBackupVersionTask): GetKeysBackupVersionTask

    @Binds
    abstract fun bindGetRoomSessionDataTask(getRoomSessionDataTask: DefaultGetRoomSessionDataTask): GetRoomSessionDataTask

    @Binds
    abstract fun bindGetRoomSessionsDataTask(getRoomSessionDataTask: DefaultGetRoomSessionsDataTask): GetRoomSessionsDataTask

    @Binds
    abstract fun bindGetSessionsDataTask(getRoomSessionDataTask: DefaultGetSessionsDataTask): GetSessionsDataTask

    @Binds
    abstract fun bindStoreRoomSessionDataTask(storeRoomSessionDataTask: DefaultStoreRoomSessionDataTask): StoreRoomSessionDataTask

    @Binds
    abstract fun bindStoreRoomSessionsDataTask(storeRoomSessionDataTask: DefaultStoreRoomSessionsDataTask): StoreRoomSessionsDataTask

    @Binds
    abstract fun bindStoreSessionsDataTask(storeRoomSessionDataTask: DefaultStoreSessionsDataTask): StoreSessionsDataTask

    @Binds
    abstract fun bindUpdateKeysBackupVersionTask(updateKeysBackupVersionTask: DefaultUpdateKeysBackupVersionTask): UpdateKeysBackupVersionTask

    @Binds
    abstract fun bindSendToDeviceTask(sendToDeviceTask: DefaultSendToDeviceTask): SendToDeviceTask

    @Binds
    abstract fun bindClaimOneTimeKeysForUsersDeviceTask(claimOneTimeKeysForUsersDevice: DefaultClaimOneTimeKeysForUsersDevice)
            : ClaimOneTimeKeysForUsersDeviceTask

    @Binds
    abstract fun bindDeleteDeviceWithUserPasswordTask(deleteDeviceWithUserPasswordTask: DefaultDeleteDeviceWithUserPasswordTask)
            : DeleteDeviceWithUserPasswordTask
}
