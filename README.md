# AccessExercise

## Features
Create an App which shows the GitHub users in a list.



### Summary

- Use MVVM architecture.
- There are two pages, ListFragment and UserDetailFragment, which can be switched through NAVIGATIOIN.
- The drop-down list will reload the data, and swipe up will load the data until the number of data reaches 100.
- WebAPI is implemented by Volley and Gson libraries.
- Use the Glide library to load images.


## Network

### GitHub API Documentation
[List](https://docs.github.com/en/rest/reference/users#list-users)   
[Pagination](https://docs.github.com/en/rest/reference/users#get-a-user)   

### Library
[Volley](https://developer.android.com/training/volley)   
[Gson](https://www.baeldung.com/kotlin/json-convert-data-class)   
[Coroutines Codelab](https://codelabs.developers.google.com/codelabs/kotlin-coroutines/?hl=da#12)   

```
viewModelScope.launch {
    mUserAPI.getUserDetail(userName, object : BaseAPI.ResultListener {
        override fun onResult(response: String?) {
            val result = try {
                mGson.fromJson(response, UserDetail::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
            _userDetailResult.postValue(result)
        }

        override fun onError(error: VolleyError) {
            _userDetailResult.postValue(null)
        }
    })
}
```

## Dependency Injection

[Koin](https://insert-koin.io/)

The dependency injection relationship must be defined in MyApplication first.

```
class MyApplication : Application(){
    private val viewModelModule = module{
        viewModel{ YourViewModel(get())} //This 'get()' is the repository that will be injected into the view model
    }

    private val repoModule = module{
        single{ YourRepository(get())} //This 'get()' is the androidContext that will be passed into the view model
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MultiLanguagesApplication)
            modules(
                listOf(
                    viewModelModule,
                     repoModule
                 )
             )
        }
    }
}
```

Currently, ViewModel will be injected uniformly in BaseActivity/BaseFragment through generic methods

```
abstract class BaseActivity<T : BaseViewModel>(clazz: KClass<T>) : AppCompatActivity() {

    val viewModel: T by viewModel(clazz = clazz)
}
```

Inherit BaseActivity / BaseFragment only need to include generics to get the corresponding ViewModel.

```
class MainActivity : BaseActivity<MainViewModel>(MainViewModel::class) {

    viewmodel.doSomthing()
}
```