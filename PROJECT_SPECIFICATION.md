## Android UI/UX

<table>
    <tr>
        <th> CRITERIA </th>
        <th> MEETS SPECIFICATIONS </th>
    </tr>
    <tr>
        <td>
            Build a navigable interface consisting of multiple screens of functionality and data.
        </td>
        <td>
            Application includes at least three screens with distinct features using either the Android Navigation Controller or Explicit Intents.<br/>
            <br/>
            The Navigation Controller is used for Fragment-based navigation and intents are utilized for Activity-based navigation.<br/>
            <br/>
            An application bundle is built to store data passed between Fragments and Activities.
        </td>
    </tr>
    <tr>
        <td>
            Construct interfaces that adhere to Android standards and display appropriately on screens of different size and resolution.
        </td>
        <td>
            Application UI effectively utilizes <b>ConstraintLayout</b> to arrange UI elements effectively and efficiently across application features, avoiding nesting layouts and maintaining a flat UI structure where possible.<br/>
            <br/>
            Data collections are displayed effectively, taking advantage of visual hierarchy and arrangement to display data in an easily consumable format.<br/>
            <br/>
            Resources are stored appropriately using the internal <b>res</b> directory to store data in appropriate locations including <b>string</b>, <b>values</b>, <b>drawables</b>, <b>colors</b>, <b>dimensions</b> and more.<br/>
            <br/>
            Every element within <b>ConstraintLayout</b> should include the <b>id</b> field and at least 1 vertical constraint.<br/>
            <br/>
            Data collections should be loaded into the application using ViewHolder pattern and appropriate View, such as <b>RecyclerView</b>.
        </td>
    </tr>
    <tr>
        <td>
            Animate UI components to better utilize screen real estate and create engaging content.
        </td>
        <td>
            Application contains at least 1 feature utilizing MotionLayout to adapt UI elements to a given function. This could include animating control elements onto and off screen, displaying and hiding a form, or animation of complex UI transitions.<br/>
            <br/>
            MotionLayout behaviors are defined in a MotionScene using one or more Transition nodes and ConstraintSet blocks.<br/>
            <br/>
            Constraints are defined within the scenes and house all layout params for the animation.
        </td>
    </tr>
</table>


## Local and Network data

<table>
    <tr>
        <th> CRITERIA </th>
        <th> MEETS SPECIFICATIONS </th>
    </tr>
    <tr>
        <td>
            Connect to and consume data from a remote data source such as a RESTful API.
        </td>
        <td>
            The Application connects to at least 1 external data source using Retrofit or other appropriate library/component and retrieves data for use within the application.<br/>
            <br/>
            Data retrieved from the remote source is held in local models with appropriate data types that are readily handled and manipulated within the application source. Helper libraries such as Moshi may be used to assist with this requirement.<br/>
            <br/>
            The application performs work and handles network requests on the appropriate threads to avoid stalling the UI.
        </td>
    </tr>
    <tr>
        <td>
            Load network resources, such as Bitmap Images, dynamically and on-demand.
        </td>
        <td>
            The Application loads remote resources asynchronously using an appropriate library such as Glide or other library/component when needed.<br/>
            <br/>
            Images display placeholder images while being loaded and handle failed network requests gracefully.<br/>
            <br/>
            All requests are performed asynchronously and handled on the appropriate threads.
        </td>
    </tr>
    <tr>
        <td>
            Store data locally on the device for use between application sessions and/or offline use.
        </td>
        <td>
            The application utilizes storage mechanisms that best fit the data stored to store data locally on the device. Example: SharedPreferences for user settings or an internal database for data persistence for application data. Libraries such as Room may be utilized to achieve this functionality.<br/>
            <br/>
            Data stored is accessible across user sessions.<br/>
            <br/>
            Data storage operations are performed on the appropriate threads as to not stall the UI thread.<br/>
            <br/>
            Data is structured with appropriate data types and scope as required by application functionality.
        </td>
    </tr>
</table>


## Android system and hardware integration

<table>
    <tr>
        <th> CRITERIA </th>
        <th> MEETS SPECIFICATIONS </th>
    </tr>
    <tr>
        <td>
            Architect application functionality using MVVM.
        </td>
        <td>
            Application separates responsibilities amongst classes and structures using the MVVM Pattern:
            <ul>
                <li>Fragments/Activities control the Views</li>
                <li>Models houses the data structures,</li>
                <li>ViewModel controls business logic.</li>
            </ul>
            Application adheres to architecture best practices, such as the observer pattern, to prevent leaking components, such as Activity Contexts, and efficiently utilize system resources.
        </td>
    </tr>
    <tr>
        <td>
            Implement logic to handle and respond to hardware and system events that impact the Android Lifecycle.
        </td>
        <td>
            Beyond MVVM, the application handles system events, such as orientation changes, application switching, notifications, and similar events gracefully including, but not limited to:
            <ul>
                <li>Storing and restoring state and information</li>
                <li>Properly handling lifecycle events in regards to behavior and functionality</li>
                <ul>
                    <li>Implement bundles to restore and save data</li>
                </ul>
                <li>Handling interaction to and from the application via Intents</li>
                <li>Handling Android Permissions</li>
            </ul>
        </td>
    </tr>
    </tr>
    <tr>
        <td>
            Utilize system hardware to provide the user with advanced functionality and features.
        </td>
        <td>
            pplication utilizes at least 1 hardware component to provide meaningful functionality to the application as a whole. Suggestion options include:
            <ul>
                <li>Camera</li>
                <li>Location</li>
                <li>Accelerometer</li>
                <li>Microphone</li>
                <li>Gesture Capture</li>
                <li>Notifications</li>
            </ul>
            Permissions to access hardware features are requested at the time of use for the feature.<br/>
            <br/>
            Behaviors are accessed only after permissions are granted.
        </td>
    </tr>
</table>




## Suggestions to Make Your Project Stand Out!

<ol>
    <li>As with any mobile application, attention to detail within the UI, including animations within the screens and/or while navigating will elevate the application presentation as a whole. Proper use of visual hierarchy and consistent implementation with Styles can assist in elevating the experience. Ensuring screen real estate is properly utilized, but not overburdened will provide a positive user experience.</li>
    <li>Caching data, when possible, to provide some level of application functionality when offline and/or to reduce the network burden of the application can help demonstrate and mirror real-world application goals found in many enterprise applications at scale. As such, elevate your project by utilizing local storage and caching on network requests when it would not deter from the application experience. Providing users with choice and customization through Shared Preferences is a great way to balance real-time data vs possible performance gains by giving power to the user.</li>
    <li>The mobile experience is all about personal needs and convenient access. The features of the application should reflect a personal need and provide functionality and features that reflect the solution to that need. When possible, think about the following considerations:
        <ul>
            <li>Does the application work for multiple users?</li>
            <li>Does the application provide value over a website or similar static content?</li>
            <li>Does the application provide a coherent user experience that effectively and intuitively guides the user’s behavior?</li>
        </ul>
    </li>
</ol>