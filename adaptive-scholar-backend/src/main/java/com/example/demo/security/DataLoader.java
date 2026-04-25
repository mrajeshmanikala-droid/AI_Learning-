package com.example.demo.security;

import com.example.demo.model.Course;
import com.example.demo.model.Lesson;
import com.example.demo.model.Question;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        Optional<User> existingAdmin = userRepository.findByEmail("admin@scholar.com");
        User admin = existingAdmin.orElse(new User());
        admin.setName("Scholar Admin");
        admin.setEmail("admin@scholar.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole("ADMIN");
        admin.setBio("System Administrator for Adaptive Scholar.");
        userRepository.save(admin);
        System.out.println("Default Admin seeded/refreshed!");

        courseRepository.deleteAll();
        System.out.println("Cleared old curriculum...");

        // 1. Re-seed MERN Course
        Course mernCourse = new Course();
        mernCourse.setTitle("Complete MERN Stack Development");
        mernCourse.setDescription("Master MongoDB, Express, React, and Node.js with real-world projects based on the ultimate curriculum.");
        mernCourse.setImage("https://images.unsplash.com/photo-1633356122544-f134324a6cee?ixlib=rb-4.0.3&auto=format&fit=crop&w=1000&q=80");
        mernCourse.setProgress(64);
        mernCourse.setModulesCount(14);
        
        String[][] mernVideos = {
            {"H-9l-gTq-C4", "MERN Stack Project | Fullstack Tutorial", "Core API & MongoDB"},
            {"JR9BeI7FY3M", "Middleware in a MERN Stack Project | REST API Tutorial", "Core API & MongoDB"},
            {"cUV3uYXEOxI", "MongoDB MERN Stack Tutorial | MERN Project Data", "Core API & MongoDB"},
            {"Iw_LHrip-iw", "MERN API Controllers & Routers | MERN Stack Project", "Core API & MongoDB"},
            {"5cc09qZK0VU", "React.js App Project | MERN Stack Tutorial", "React Frontend"},
            {"TPAAQnVxc-I", "MERN Stack Project with React Redux and RTK Query", "React Frontend"},
            {"H3Vo2NJgPvQ", "React Forms with Redux & RTK Query | MERN Stack Tutorial", "React Frontend"},
            {"4TtAGhr61VI", "MERN Stack Authentication with JWT Access, Refresh Tokens, Cookies", "Auth & Security"},
            {"PDJm1Hwx0oo", "Login Authentication in React.js with Redux | MERN Stack Project", "Auth & Security"},
            {"9YnZHQsWmJs", "JWT Authentication | Persist Login State on Refresh | MERN Stack", "Auth & Security"},
            {"UhrmPH3TLus", "User Role-Based Access Control & Permissions in React JS | MERN Stack", "Advanced Topics"},
            {"jEVyPJ3U_y0", "Code Refactoring in React, Node.js, Express, MongoDB | MERN Stack Project", "Advanced Topics"},
            {"l134cBAJCuc", "Deploy a Full Stack App - React, Node.js, Express, Mongo | MERN Tutorial", "Advanced Topics"},
            {"CvCiNeLnZ00", "MERN Stack Full Tutorial & Project | Complete All-in-One Course | 8 Hours", "Advanced Topics"}
        };

        List<Lesson> mernLessons = new ArrayList<>();
        for (String[] v : mernVideos) {
            Lesson l = new Lesson();
            l.setVideoUrl(v[0]);
            l.setTitle(v[1]);
            l.setModule(v[2]);
            l.setDescription("Learn: " + v[1]);
            l.setDuration("45:00");
            l.setCourse(mernCourse);
            mernLessons.add(l);
        }
        mernCourse.setLessons(mernLessons);
        courseRepository.save(mernCourse);

        // 2. Seed Next-Gen AI Course
        Course aiCourse = new Course();
        aiCourse.setTitle("Next-Gen AI: Mastering DeepSeek, LLMs, and Generative Tools");
        aiCourse.setDescription("Comprehensive journey from basic AI concepts to advanced Deep Learning, LLMs, and DeepSeek tools.");
        aiCourse.setImage("https://images.unsplash.com/photo-1677442136019-21780ecad995?auto=format&fit=crop&q=80&w=800"); 
        aiCourse.setProgress(12);
        aiCourse.setModulesCount(60);

        String[][] aiVideos = {
            {"4jmsHaJ7xEA", "Artificial Intelligence | What is AI | Introduction to Artificial Intelligence", "01: AI Fundamentals"},
            {"JMUxmLyrhSk", "Artificial Intelligence Full Course | Artificial Intelligence Tutorial for Beginners", "01: AI Fundamentals"},
            {"UFDOY1wOOz0", "Artificial Intelligence in 2 Minutes | What is Artificial Intelligence?", "01: AI Fundamentals"},
            {"5hNK7-N23eU", "Introduction to Artificial Intelligence | Deep Learning", "01: AI Fundamentals"},
            {"0vfZFL-ftz0", "What is Artificial Intelligence | Artificial Intelligence Tutorial For Beginners", "01: AI Fundamentals"},
            {"Y46zXHvUB1s", "Top 10 Applications Of Artificial Intelligence in 2021", "01: AI Fundamentals"},
            {"WZVAfLreIwM", "Artificial Intelligence Tutorial for Beginners | Artificial Intelligence Explained", "01: AI Fundamentals"},
            {"DhdUlDIAG7Y", "Q Learning Explained | Reinforcement Learning Using Python | Q Learning in AI", "02: Machine Learning"},
            {"oV74Najm6Nc", "What Is Artificial Intelligence? | Artificial Intelligence (AI) In 10 Minutes", "02: Machine Learning"},
            {"Pj0neYUp9Tc", "What is Machine Learning? | Machine Learning Basics | Machine Learning Tutorial", "02: Machine Learning"},
            {"PbCl67GY1ck", "Breadth First Search Algorithm In 10 Minutes | BFS in Artificial Intelligence", "02: Machine Learning"},
            {"masnR4-vt3M", "Top 10 Benefits Of Artificial Intelligence in 2021 | Artificial Intelligence Advantages", "02: Machine Learning"},
            {"dafuAz_CV7Q", "What is Deep Learning | Deep Learning Simplified | Deep Learning Tutorial", "03: Deep Learning"},
            {"WSbgixdC9g8", "AI vs Machine Learning vs Deep Learning | AI vs ML vs DL - Differences Explained", "03: Deep Learning"},
            {"nl_4WFHQ4LU", "Deep Learning Tutorial | Deep Learning Tutorial for Beginners | Neural Networks", "03: Deep Learning"},
            {"yX8KuPZCAMo", "TensorFlow Tutorial | Deep Learning Using TensorFlow | TensorFlow Tutorial Python", "03: Deep Learning"},
            {"fv6Qll3laUU", "Artificial Neural Network Tutorial | Deep Learning With Neural Networks", "04: Neural Networks"},
            {"y7qrilE-Zlc", "Recurrent Neural Networks (RNN) | RNN LSTM | Deep Learning Tutorial | Tensorflow Tutorial", "04: Neural Networks"},
            {"umGJ30-15_A", "Convolutional Neural Network (CNN) | Convolutional Neural Networks With TensorFlow", "04: Neural Networks"},
            {"VpUKOLtqBSA", "Deep Learning Using TensorFlow | Deep Learning with Tensorflow Certification Training", "04: Neural Networks"},
            {"uh2Fh6df7Lg", "TensorFlow Explained | Deep Learning Using TensorFlow | TensorFlow Tutorial", "05: Advanced Tooling"},
            {"wh7_etX91ls", "TensorFlow Object Detection | Realtime Object Detection with TensorFlow", "05: Advanced Tooling"},
            {"XNKeayZW4dY", "Keras Tutorial For Beginners | Creating Deep Learning Models Using Keras In Python", "05: Advanced Tooling"},
            {"y5swZ2Q_lBw", "Types Of Artificial Intelligence | Artificial Intelligence Explained | What is AI?", "05: Advanced Tooling"},
            {"7O60HOZRLng", "Artificial Intelligence with Python | Artificial Intelligence Tutorial using Python", "06: AI in Practice"},
            {"V-O-RFSRe-E", "Knowledge Representation in AI | Semantic Networks | Artificial Intelligence Tutorial", "06: AI in Practice"},
            {"_ThdIOA9Lbk", "Hill Climbing Algorithm | Hill Climbing in Artificial Intelligence | Data Science Tutorial", "06: AI in Practice"},
            {"amlkE0g-YFU", "A* Algorithm in AI | A Star Search Algorithm | Artificial Intelligence Tutorial", "06: AI in Practice"},
            {"K_Mh21P9OwA", "Top 10 Artificial Intelligence Technologies in 2020 | Artificial Intelligence Trends", "06: AI in Practice"},
            {"XCI5j_oEWmQ", "The Future of AI | How will Artificial Intelligence Change the World in 2020?", "07: The Future Landscape"},
            {"Zsl7ttA9Kcg", "What is Cognitive AI? Cognitive Computing vs Artificial Intelligence | AI Tutorial", "07: The Future Landscape"},
            {"JFB_751d2uc", "Future of AI/ML | Rise Of Artificial Intelligence & Machine Learning", "07: The Future Landscape"},
            {"Y2vMNCNM4_c", "AI in Space Science | Future of Artificial Intelligence | Artificial Intelligence Training", "07: The Future Landscape"},
            {"-NTzW2Jvris", "How to become an Artificial Intelligence Engineer | AI Engineer Skills | Python Tutorial", "08: AI Careers"},
            {"bZwpXEgG0to", "AI Roadmap For 2021 | Learn AI | Artificial Intelligence Careers | Data Science Training", "08: AI Careers"},
            {"FgzDw23vPDg", "AI Roadmap For 2021 | Learn AI | Artificial Intelligence Careers | Edureka Rewind", "08: AI Careers"},
            {"oB1-27Qk4sU", "Artificial Intelligence Tutorial for beginners | Edureka | Deep Learning Rewind - 3", "08: AI Careers"},
            {"aESz_y6U_x4", "Recurrent Neural Networks Tutorial | Tensorflow Tutorial | Edureka | Deep Learning Rewind - 4", "08: AI Careers"},
            {"GxhZs9X0YK8", "The Future of AI | How will Artificial Intelligence Change the World in 2020? | Edureka Rewind", "09: Review Seminars"},
            {"spnQq0HXZVc", "How AI is changing the Gaming Industry in 2021 | Artificial Intelligence Training", "09: Review Seminars"},
            {"XXr5RnWzskA", "AI vs ML vs Deep Learning | Difference between AI, ML and DL | AI-ML Training", "09: Review Seminars"},
            {"cvCG5SdInZE", "How to become an AI Engineer | Artificial Intelligence Roadmap | AI Career Path", "09: Review Seminars"},
            {"7V_AebZFlpo", "Artificial Intelligence Project Ideas | Artificial Intelligence Training", "09: Review Seminars"},
            {"KvgCq9KsnAE", "Dangers of AI | How Dangerous is Artificial Intelligence | Learn AI", "10: Modern Concepts"},
            {"3M1wUK2zCKY", "Hierarchical Clustering | Agglomerative and Divisive Hierarchical Clustering Explained", "10: Modern Concepts"},
            {"egOUSobY6N4", "Water Jug Problem in Artificial Intelligence | State Representation & Solution", "10: Modern Concepts"},
            {"eciABhWBiUc", "Feature selection in Machine Learning | Feature Selection Techniques with Examples", "10: Modern Concepts"},
            {"VOaoabf3LPM", "Artificial Intelligence Full Course in 10 Hours [2024] | Artificial Intelligence Tutorial", "10: Modern Concepts"},
            {"gMa_QHSAxOY", "Top 10 Generative AI Tools | Generative AI Tools | AI Edureka", "11: Generative AI"},
            {"r9F-SjS5Ec0", "Hands-on Rust | Rust Tutorial | Rust Programming Language | Edureka", "11: Generative AI"},
            {"_JzMe6ijR0A", "Firebase Firestore Tutorial | Basics of Firestore | Edureka", "11: Generative AI"},
            {"7HfRNqRCkBc", "Artificial Intelligence Full Course in 10 Hours [2024] | Artificial Intelligence Tutorial", "11: Generative AI"},
            {"THtGV_9lc68", "Artificial Intelligence Full Course - 10 Hours | Artificial Intelligence Tutorial [2024]", "11: Generative AI"},
            {"dBvdGJVF4ic", "Midjourney AI Tutorial | Learn How To Use Midjourney AI Under 10 Minutes", "11: Generative AI"},
            {"lAkjqDk4J4k", "AI Tutorial for Beginners | Artificial Intelligence (AI) for Beginners | AI Explained", "12: DeepSeek & Ethics"},
            {"vck4kXGV63o", "AI for Business | How AI Could Empower Businesses | AI for Business Leaders", "12: DeepSeek & Ethics"},
            {"COgvUFGLPCs", "What Is AI Ethics | AI Ethics Foundation | How to Implement AI Ethics", "12: DeepSeek & Ethics"},
            {"e2_ftgGLTss", "AI and Industry 4.0 | Industry 4.0 with Artificial Intelligence | What is Industry 4.0", "12: DeepSeek & Ethics"},
            {"HA4G14DCiT0", "AI for Marketing |Top AI Marketing Tools | Beginner's Guide to AI Marketing", "12: DeepSeek & Ethics"},
            {"FeNv9mQe8Vk", "Artificial Intelligence Full Course - 10 Hours | Artificial Intelligence Tutorial", "12: DeepSeek & Ethics"},
            {"88rBTI75wYA", "DeepSeek vs OpenAI: Who Wins the AI Race? | DeepSeek R1 vs ChatGPT o1", "13: Mastery 2026"},
            {"tNY31oAgELg", "Top 5 AI Frameworks in 2025 | AI Frameworks to Learn in 2026 | Edureka", "13: Mastery 2026"},
            {"WJM1NeCl2Aw", "Top 15 AI Skills You Need to Know in 2026 | 15 AI Skills You NEED NOW", "13: Mastery 2026"},
            {"TSErdn-wBSI", "AI Engineer Learning Path 2026 | AI Engineer Roadmap | How to Become an AI Engineer", "13: Mastery 2026"},
            {"uRGEEULzzik", "AI & ML Engineer Roadmap For 2026 | Step-by-Step Guide for Beginners | AI & ML Roadmap", "13: Mastery 2026"},
            {"BXoKOb6TJlw", "AI Engineer Roadmap 2026 \uD83D\uDE80 | Step-by-Step", "13: Mastery 2026"}
        };

        List<Lesson> aiLessons = new ArrayList<>();
        for (String[] v : aiVideos) {
            Lesson l = new Lesson();
            l.setVideoUrl(v[0]);
            l.setTitle(v[1]);
            l.setModule(v[2]);
            l.setDescription("Explore: " + v[1]);
            l.setDuration("45:00");
            l.setCourse(aiCourse);
            aiLessons.add(l);
        }
        aiCourse.setLessons(aiLessons);
        courseRepository.save(aiCourse);
        
        System.out.println("Database successfully seeded with 2 Massive Advanced Courses!");

        // 3. Seed MERN Stack Quiz Questions
        Question q1 = new Question();
        q1.setText("What does MERN stand for?");
        q1.setOptions(Arrays.asList("MongoDB, Express, React, Node", "MySQL, Express, React, Node", "MongoDB, Ember, React, Node", "MariaDB, Express, Ruby, Node"));
        q1.setCorrectOptionIndex(0);
        q1.setExplanation("MERN is a software stack that includes MongoDB, Express.js, React, and Node.js.");
        q1.setCourse(mernCourse);

        Question q2 = new Question();
        q2.setText("Which port does a React development server typically run on by default?");
        q2.setOptions(Arrays.asList("8080", "4200", "3000", "5000"));
        q2.setCorrectOptionIndex(2);
        q2.setExplanation("Create React App and Vite typically default to port 3000 or 5173, but historically React uses 3000.");
        q2.setCourse(mernCourse);

        Question q3 = new Question();
        q3.setText("In MongoDB, what is the equivalent of a Table in a relational database?");
        q3.setOptions(Arrays.asList("Row", "Document", "Collection", "Column"));
        q3.setCorrectOptionIndex(2);
        q3.setExplanation("A Collection in MongoDB groups related documents together, similar to how a Table groups rows in SQL.");
        q3.setCourse(mernCourse);

        Question q4 = new Question();
        q4.setText("Which package is commonly used to create models and schemas for MongoDB in a Node.js environment?");
        q4.setOptions(Arrays.asList("Sequelize", "Mongoose", "Prisma", "TypeORM"));
        q4.setCorrectOptionIndex(1);
        q4.setExplanation("Mongoose provides a straight-forward, schema-based solution to model user data for MongoDB.");
        q4.setCourse(mernCourse);

        Question q5 = new Question();
        q5.setText("What hook in React is used to perform side effects in functional components?");
        q5.setOptions(Arrays.asList("useState", "useMemo", "useEffect", "useReducer"));
        q5.setCorrectOptionIndex(2);
        q5.setExplanation("useEffect is the hook designed to handle side effects like data fetching or manual DOM mutations.");
        q5.setCourse(mernCourse);

        questionRepository.saveAll(Arrays.asList(q1, q2, q3, q4, q5));
        
        Question q6 = new Question();
        q6.setText("What is the main purpose of Express.js in the MERN stack?");
        q6.setOptions(Arrays.asList("Managing database clusters", "Handling client-side rendering", "Building RESTful web services and APIs", "Compiling React code into plain Javascript"));
        q6.setCorrectOptionIndex(2);
        q6.setExplanation("Express is a minimal native backend framework for Node.js commonly used to build APIs and handle server-side routing.");
        q6.setCourse(mernCourse);

        Question q7 = new Question();
        q7.setText("Which React hook should be used to manage complex component state logic with multiple sub-values?");
        q7.setOptions(Arrays.asList("useContext", "useReducer", "useCallback", "useRef"));
        q7.setCorrectOptionIndex(1);
        q7.setExplanation("useReducer is preferable to useState when you have complex state logic that involves multiple sub-values or when the next state depends on the previous one.");
        q7.setCourse(mernCourse);

        Question q8 = new Question();
        q8.setText("How does Node.js handle concurrency despite being single-threaded?");
        q8.setOptions(Arrays.asList("It spins up multiple threads under the hood using a thread-pool.", "It uses a non-blocking asynchronous event loop.", "It relies on the operating system to distribute requests.", "It blocks the thread until IO operations finish."));
        q8.setCorrectOptionIndex(1);
        q8.setExplanation("Node relies heavily on its Event Loop to execute non-blocking operations asynchronously, assigning heavy I/O to system layers.");
        q8.setCourse(mernCourse);

        Question q9 = new Question();
        q9.setText("In Express, how do you handle incoming JSON payload data from a POST request body?");
        q9.setOptions(Arrays.asList("app.use(express.json())", "app.parse(JSON)", "require('json-parser')", "It is done automatically by default."));
        q9.setCorrectOptionIndex(0);
        q9.setExplanation("Since Express v4.16+, you must use the built-in express.json() middleware to recognize the incoming Request Object as a JSON Object.");
        q9.setCourse(mernCourse);

        Question q10 = new Question();
        q10.setText("What is a correct way to start a new React project in 2024?");
        q10.setOptions(Arrays.asList("npx create-react-app my-app", "npm init react-project", "npm create vite@latest my-app -- --template react", "react init my-app"));
        q10.setCorrectOptionIndex(2);
        q10.setExplanation("While CRA used to be standard, Vite or frameworks like Next.js are the modern recommended approaches for initiating a React tree.");
        q10.setCourse(mernCourse);

        questionRepository.saveAll(Arrays.asList(q6, q7, q8, q9, q10));
        
        // 4. Seed Next-Gen AI Quiz Questions
        Question q11 = new Question();
        q11.setText("What is the primary function of the Activation Function in a multi-layer perceptron?");
        q11.setOptions(Arrays.asList("To linearize the output of each neuron layer.", "To introduce non-linearity, allowing the model to learn complex patterns.", "To normalize the weights before the backpropagation step.", "To prevent overfitting by dropping random neurons."));
        q11.setCorrectOptionIndex(1);
        q11.setExplanation("Without non-linear activation functions, a deep neural network behaves exactly like a single-layer linear regression model.");
        q11.setCourse(aiCourse);

        Question q12 = new Question();
        q12.setText("In transformer architectures (like GPT), what is the purpose of the 'Self-Attention' mechanism?");
        q12.setOptions(Arrays.asList("To align input images with output labels.", "To dynamically weigh the importance of different words in a sequence regardless of distance.", "To perform gradient clipping automatically.", "To compress the model weights for mobile devices."));
        q12.setCorrectOptionIndex(1);
        q12.setExplanation("Self-attention allows the model to look at the entire sequence and assign a weight to each token to understand context and relationships.");
        q12.setCourse(aiCourse);

        Question q13 = new Question();
        q13.setText("Which algorithm is primarily used for training deep neural networks by calculating the gradient of the loss function?");
        q13.setOptions(Arrays.asList("K-Means Clustering", "Backpropagation", "Random Forest", "Principal Component Analysis"));
        q13.setCorrectOptionIndex(1);
        q13.setExplanation("Backpropagation computes the gradient of the loss function with respect to the weights by applying the chain rule of calculus.");
        q13.setCourse(aiCourse);

        Question q14 = new Question();
        q14.setText("In reinforcement learning, what is the entity that takes actions in an environment to maximize cumulative reward?");
        q14.setOptions(Arrays.asList("The Generator", "The Discriminator", "The Agent", "The Oracle"));
        q14.setCorrectOptionIndex(2);
        q14.setExplanation("The Agent is the decision-maker in RL that learns an optimum policy by interacting with the environment.");
        q14.setCourse(aiCourse);

        Question q15 = new Question();
        q15.setText("What architecture is composed of a Generator and a Discriminator competing against each other?");
        q15.setOptions(Arrays.asList("Recurrent Neural Networks (RNN)", "Convolutional Neural Networks (CNN)", "Generative Adversarial Networks (GAN)", "Autoencoders"));
        q15.setCorrectOptionIndex(2);
        q15.setExplanation("GANs consist of a generator creating fake data and a discriminator trying to distinguish it from real data in a zero-sum game.");
        q15.setCourse(aiCourse);

        Question q16 = new Question();
        q16.setText("What does the 'learning rate' hyperparameter control in gradient descent?");
        q16.setOptions(Arrays.asList("The number of layers in the network.", "The size of the steps taken towards the minimum of the loss function.", "The number of epochs it takes to overfit.", "The batch size applied to the data loader."));
        q16.setCorrectOptionIndex(1);
        q16.setExplanation("Learning rate determines how drastically the model's weights are updated during each iteration of backpropagation.");
        q16.setCourse(aiCourse);

        Question q17 = new Question();
        q17.setText("Which NLP tokenization technique breaks words into sub-words to handle out-of-vocabulary terms effectively?");
        q17.setOptions(Arrays.asList("Word2Vec", "Byte-Pair Encoding (BPE)", "Bag-of-Words", "TF-IDF"));
        q17.setCorrectOptionIndex(1);
        q17.setExplanation("BPE (and WordPiece/SentencePiece) merged frequent pairs of characters into sub-words, dramatically reducing vocabulary size and handling unknown words.");
        q17.setCourse(aiCourse);

        Question q18 = new Question();
        q18.setText("What problem do 'Skip Connections' (Residual connections) solve effectively in very deep networks?");
        q18.setOptions(Arrays.asList("Vanishing Gradient Problem", "Exploding Gradient Problem", "Dataset Imbalance", "Memory Leaks"));
        q18.setCorrectOptionIndex(0);
        q18.setExplanation("Residual connections provide an alternative shortcut path for gradients to bypass layers, directly combatting the vanishing gradient problem.");
        q18.setCourse(aiCourse);

        Question q19 = new Question();
        q19.setText("In Large Language Models, what is 'Temperature' used to control during generation?");
        q19.setOptions(Arrays.asList("The maximum sequence length.", "The hardware GPU thermal limits.", "The randomness and creativity of the generated tokens.", "The precision of the floating point weights."));
        q19.setCorrectOptionIndex(2);
        q19.setExplanation("Higher temperature flattens the prompt probability distribution resulting in more diverse/random outputs. Lower temperature makes it more deterministic.");
        q19.setCourse(aiCourse);

        Question q20 = new Question();
        q20.setText("What technique modifies a pre-trained LLM using human feedback to better align with user instructions?");
        q20.setOptions(Arrays.asList("Data Augmentation", "RLHF (Reinforcement Learning from Human Feedback)", "K-Fold Cross Validation", "Singular Value Decomposition"));
        q20.setCorrectOptionIndex(1);
        q20.setExplanation("RLHF trains a reward model based on human preferences and using PPO to optimize the language model's policy.");
        q20.setCourse(aiCourse);

        questionRepository.saveAll(Arrays.asList(q11, q12, q13, q14, q15, q16, q17, q18, q19, q20));
        System.out.println("Database successfully seeded with AI Quiz Content!");

        // 3. Re-seed SQL & Database Mastery Course
        Course sqlCourse = new Course();
        sqlCourse.setTitle("SQL & Database Mastery");
        sqlCourse.setDescription("Master SQL from scratch — covering MySQL, PostgreSQL, normalization, joins, subqueries, and real-world data analysis projects.");
        sqlCourse.setImage("/assets/images/sql-cover.png");
        sqlCourse.setProgress(0);
        sqlCourse.setModulesCount(5);

        List<Lesson> sqlLessons = new ArrayList<>();

        // Module 1: SQL Foundations
        sqlLessons.add(createLesson("What is SQL? Introduction to SQL", "A beginner-friendly introduction to SQL databases and their role in modern applications.", "pFq1pgli0OQ", "15 min", "SQL Foundations", sqlCourse));
        sqlLessons.add(createLesson("SQL Basics For Beginners", "Learn the basic SQL syntax, data types, and fundamental operations.", "DvNHkJAR0BM", "20 min", "SQL Foundations", sqlCourse));
        sqlLessons.add(createLesson("How to Create a Database in SQL", "Step-by-step guide to creating your first database and tables.", "1n9Z_ytgoUI", "18 min", "SQL Foundations", sqlCourse));
        sqlLessons.add(createLesson("Create, Insert, and Select in SQL", "Master the essential CREATE TABLE, INSERT, and SELECT statements.", "jPa2XbfVDZc", "22 min", "SQL Foundations", sqlCourse));

        // Module 2: CRUD & Table Operations
        sqlLessons.add(createLesson("Update Records in SQL Tables", "Learn how to modify existing data using UPDATE queries.", "mWmsBmWnSiY", "16 min", "CRUD Operations", sqlCourse));
        sqlLessons.add(createLesson("Delete Operations in SQL", "Understand DELETE and TRUNCATE operations for data removal.", "e_Qnh2-zcqI", "14 min", "CRUD Operations", sqlCourse));
        sqlLessons.add(createLesson("ALTER Table: Add, Drop, Rename Columns", "Modify table structure using ALTER TABLE statements.", "LCpw7M2bVtI", "20 min", "CRUD Operations", sqlCourse));
        sqlLessons.add(createLesson("How to Delete Duplicate Records", "Techniques for identifying and removing duplicate data.", "KBQQFjduFag", "18 min", "CRUD Operations", sqlCourse));

        // Module 3: Intermediate SQL
        sqlLessons.add(createLesson("SQL Joins with Examples", "Master INNER, LEFT, RIGHT, and FULL OUTER joins.", "vncBSUNb4NA", "25 min", "Intermediate SQL", sqlCourse));
        sqlLessons.add(createLesson("Group By and Having Clause", "Learn data aggregation with GROUP BY and filtering with HAVING.", "jk6_L0k8VPg", "22 min", "Intermediate SQL", sqlCourse));
        sqlLessons.add(createLesson("Subqueries in SQL", "Write nested queries for complex data retrieval scenarios.", "JksrTuEVEPk", "20 min", "Intermediate SQL", sqlCourse));
        sqlLessons.add(createLesson("BETWEEN and LIKE Operators", "Master pattern matching and range filtering in SQL.", "zek0EtE4FoM", "15 min", "Intermediate SQL", sqlCourse));

        // Module 4: Advanced SQL
        sqlLessons.add(createLesson("Views in SQL Explained", "Create virtual tables using SQL Views for simplified queries.", "XmI5SwzGC8Q", "20 min", "Advanced SQL", sqlCourse));
        sqlLessons.add(createLesson("Triggers in DBMS", "Automate database operations with triggers.", "rIi1dvPdTHE", "22 min", "Advanced SQL", sqlCourse));
        sqlLessons.add(createLesson("Database Normalization 1NF 2NF 3NF", "Understand normalization forms for optimal database design.", "cJPVceqfF8I", "25 min", "Advanced SQL", sqlCourse));
        sqlLessons.add(createLesson("Temporary Tables in SQL", "Create and use temporary tables for complex operations.", "eaX0f--5Eew", "18 min", "Advanced SQL", sqlCourse));

        // Module 5: Real-World & Career
        sqlLessons.add(createLesson("SQL For Data Analysis", "Apply SQL skills to real-world data analysis scenarios.", "L6Yo8-JiXRg", "30 min", "Real-World SQL", sqlCourse));
        sqlLessons.add(createLesson("SQL vs NoSQL Databases", "Compare relational and NoSQL databases for different use cases.", "jh14LlMHyds", "20 min", "Real-World SQL", sqlCourse));
        sqlLessons.add(createLesson("SQL Project For Data Analysis", "End-to-end SQL project with real datasets.", "SAWiIV12sU4", "35 min", "Real-World SQL", sqlCourse));
        sqlLessons.add(createLesson("SQL Interview Questions & Answers", "Prepare for SQL interviews with top questions and strategies.", "L-URbfgxBMQ", "25 min", "Real-World SQL", sqlCourse));

        sqlCourse.setLessons(sqlLessons);
        courseRepository.save(sqlCourse);
        System.out.println("SQL & Database Mastery course seeded with " + sqlLessons.size() + " lessons!");

        // SQL Quiz Questions (static fallback)
        Question sq1 = new Question();
        sq1.setText("Which SQL clause is used to filter rows returned by a SELECT statement?");
        sq1.setOptions(Arrays.asList("ORDER BY", "GROUP BY", "WHERE", "HAVING"));
        sq1.setCorrectOptionIndex(2);
        sq1.setExplanation("WHERE filters rows before grouping. HAVING filters after GROUP BY.");
        sq1.setCourse(sqlCourse);

        Question sq2 = new Question();
        sq2.setText("What type of JOIN returns all rows from both tables, matching where possible?");
        sq2.setOptions(Arrays.asList("INNER JOIN", "LEFT JOIN", "RIGHT JOIN", "FULL OUTER JOIN"));
        sq2.setCorrectOptionIndex(3);
        sq2.setExplanation("FULL OUTER JOIN combines LEFT and RIGHT joins, returning all rows from both tables.");
        sq2.setCourse(sqlCourse);

        Question sq3 = new Question();
        sq3.setText("Which normal form eliminates transitive dependencies?");
        sq3.setOptions(Arrays.asList("1NF", "2NF", "3NF", "BCNF"));
        sq3.setCorrectOptionIndex(2);
        sq3.setExplanation("3NF removes transitive dependencies — non-key columns must depend only on the primary key.");
        sq3.setCourse(sqlCourse);

        Question sq4 = new Question();
        sq4.setText("What does the SQL DISTINCT keyword do?");
        sq4.setOptions(Arrays.asList("Sorts results", "Removes duplicate rows", "Limits result count", "Groups data"));
        sq4.setCorrectOptionIndex(1);
        sq4.setExplanation("DISTINCT eliminates duplicate rows from the result set.");
        sq4.setCourse(sqlCourse);

        Question sq5 = new Question();
        sq5.setText("Which aggregate function returns the number of rows that match a condition?");
        sq5.setOptions(Arrays.asList("SUM()", "AVG()", "COUNT()", "MAX()"));
        sq5.setCorrectOptionIndex(2);
        sq5.setExplanation("COUNT() returns the number of rows matching the specified criteria.");
        sq5.setCourse(sqlCourse);

        Question sq6 = new Question();
        sq6.setText("What is a subquery in SQL?");
        sq6.setOptions(Arrays.asList("A query that runs after the main query", "A query nested inside another query", "A query that modifies data", "A stored procedure"));
        sq6.setCorrectOptionIndex(1);
        sq6.setExplanation("A subquery is a SELECT statement embedded within another SQL statement.");
        sq6.setCourse(sqlCourse);

        Question sq7 = new Question();
        sq7.setText("Which SQL statement is used to modify existing records in a table?");
        sq7.setOptions(Arrays.asList("ALTER", "MODIFY", "UPDATE", "CHANGE"));
        sq7.setCorrectOptionIndex(2);
        sq7.setExplanation("UPDATE is used to modify existing data in a table using SET clause.");
        sq7.setCourse(sqlCourse);

        Question sq8 = new Question();
        sq8.setText("What is the purpose of an INDEX in SQL?");
        sq8.setOptions(Arrays.asList("To enforce unique constraints", "To speed up data retrieval", "To create backups", "To encrypt data"));
        sq8.setCorrectOptionIndex(1);
        sq8.setExplanation("Indexes create a data structure that allows the database engine to find rows faster.");
        sq8.setCourse(sqlCourse);

        Question sq9 = new Question();
        sq9.setText("Which SQL keyword is used to sort query results?");
        sq9.setOptions(Arrays.asList("SORT BY", "GROUP BY", "ORDER BY", "ARRANGE BY"));
        sq9.setCorrectOptionIndex(2);
        sq9.setExplanation("ORDER BY sorts the result set in ascending (ASC) or descending (DESC) order.");
        sq9.setCourse(sqlCourse);

        Question sq10 = new Question();
        sq10.setText("What does a FOREIGN KEY constraint enforce?");
        sq10.setOptions(Arrays.asList("Column uniqueness", "Referential integrity between tables", "Column not null", "Auto-increment behavior"));
        sq10.setCorrectOptionIndex(1);
        sq10.setExplanation("A FOREIGN KEY links two tables together, ensuring referential integrity by matching a column to a PRIMARY KEY in another table.");
        sq10.setCourse(sqlCourse);

        questionRepository.saveAll(Arrays.asList(sq1, sq2, sq3, sq4, sq5, sq6, sq7, sq8, sq9, sq10));
        System.out.println("SQL Quiz Questions seeded!");

        // 4. Re-seed Python Programming Masterclass Course
        Course pythonCourse = new Course();
        pythonCourse.setTitle("Python Programming Masterclass");
        pythonCourse.setDescription("Learn Python from scratch — covering fundamentals, OOP, data structures, data analysis with Pandas & NumPy, and real-world projects.");
        pythonCourse.setImage("/assets/images/python-cover.png");
        pythonCourse.setProgress(0);
        pythonCourse.setModulesCount(5);

        List<Lesson> pyLessons = new ArrayList<>();

        // Module 1: Python Foundations
        pyLessons.add(createLesson("Introduction to Python", "A beginner-friendly introduction to Python and its ecosystem.", "_xQNeOTRyig", "25 min", "Python Foundations", pythonCourse));
        pyLessons.add(createLesson("Installing Python on Windows", "Step-by-step guide to installing Python and setting up your IDE.", "Pc-fBj78lDM", "15 min", "Python Foundations", pythonCourse));
        pyLessons.add(createLesson("Your First Python Program", "Write and run your first Python program from scratch.", "HcUpnurp2sQ", "18 min", "Python Foundations", pythonCourse));
        pyLessons.add(createLesson("Python Data Types", "Understand integers, floats, strings, booleans and type conversion.", "MoZFRj0C2vg", "20 min", "Python Foundations", pythonCourse));

        // Module 2: Core Concepts
        pyLessons.add(createLesson("Variables in Python", "Learn how to declare, assign, and use variables effectively.", "kwNx8zVw6LM", "16 min", "Core Concepts", pythonCourse));
        pyLessons.add(createLesson("Strings in Python", "Master string operations, slicing, formatting, and methods.", "JbmpyhuEjW0", "22 min", "Core Concepts", pythonCourse));
        pyLessons.add(createLesson("Operators in Python", "Arithmetic, comparison, logical, and bitwise operators explained.", "2hHs9rWH81g", "18 min", "Core Concepts", pythonCourse));
        pyLessons.add(createLesson("Lists, Tuples, and Dictionaries", "Master Python's core data structures with practical examples.", "fXRxHrDhQuI", "25 min", "Core Concepts", pythonCourse));

        // Module 3: Control Flow & Functions
        pyLessons.add(createLesson("If Statement & Conditionals", "Learn conditional logic with if, elif, and else statements.", "ft8nK7NSLig", "20 min", "Control Flow", pythonCourse));
        pyLessons.add(createLesson("For Loops in Python", "Iterate through sequences using for loops and range().", "u6ZmnyIkOgk", "22 min", "Control Flow", pythonCourse));
        pyLessons.add(createLesson("Lambda Functions", "Write concise anonymous functions with lambda expressions.", "P8MdDCTbMOI", "18 min", "Control Flow", pythonCourse));
        pyLessons.add(createLesson("Map, Filter, and Reduce", "Functional programming techniques for data transformation.", "nMS5ptwax08", "20 min", "Control Flow", pythonCourse));

        // Module 4: OOP & Advanced Python
        pyLessons.add(createLesson("Classes in Python", "Create classes, objects, and understand constructors.", "2WFg2JCaMuc", "25 min", "OOP & Advanced", pythonCourse));
        pyLessons.add(createLesson("OOP in Python", "Master encapsulation, abstraction, and polymorphism.", "jaie0C-uZug", "28 min", "OOP & Advanced", pythonCourse));
        pyLessons.add(createLesson("Inheritance in Python", "Types of inheritance and method resolution order.", "DJqWNfu0guk", "22 min", "OOP & Advanced", pythonCourse));
        pyLessons.add(createLesson("File Handling in Python", "Read, write, and manage files using Python.", "DmHSwTiD5Tk", "20 min", "OOP & Advanced", pythonCourse));

        // Module 5: Data Science & Projects
        pyLessons.add(createLesson("NumPy and Pandas Tutorial", "Data manipulation with Python's most popular libraries.", "FniLzpaSFGk", "30 min", "Data Science", pythonCourse));
        pyLessons.add(createLesson("Python Data Visualization", "Create stunning charts with Matplotlib and Seaborn.", "Nt84_TzRkbo", "25 min", "Data Science", pythonCourse));
        pyLessons.add(createLesson("Spotify Data Analysis Project", "End-to-end data analysis project using real Spotify data.", "8d7ywKCm6HI", "35 min", "Data Science", pythonCourse));
        pyLessons.add(createLesson("Exception Handling", "Handle errors gracefully with try, except, and finally.", "-_uNayxaQoU", "18 min", "Data Science", pythonCourse));

        pythonCourse.setLessons(pyLessons);
        courseRepository.save(pythonCourse);
        System.out.println("Python Programming Masterclass seeded with " + pyLessons.size() + " lessons!");

        // Python Quiz Questions (static fallback)
        Question pq1 = new Question();
        pq1.setText("What is the output of print(type(10)) in Python?");
        pq1.setOptions(Arrays.asList("<class 'float'>", "<class 'int'>", "<class 'str'>", "<class 'number'>"));
        pq1.setCorrectOptionIndex(1);
        pq1.setExplanation("In Python, 10 is an integer literal, so type(10) returns <class 'int'>.");
        pq1.setCourse(pythonCourse);

        Question pq2 = new Question();
        pq2.setText("Which keyword is used to define a function in Python?");
        pq2.setOptions(Arrays.asList("function", "func", "def", "define"));
        pq2.setCorrectOptionIndex(2);
        pq2.setExplanation("The 'def' keyword is used to define functions in Python.");
        pq2.setCourse(pythonCourse);

        Question pq3 = new Question();
        pq3.setText("What is the difference between a list and a tuple in Python?");
        pq3.setOptions(Arrays.asList("Lists are immutable, tuples are mutable", "Tuples are immutable, lists are mutable", "Both are mutable", "Both are immutable"));
        pq3.setCorrectOptionIndex(1);
        pq3.setExplanation("Lists are mutable (can be changed), while tuples are immutable (cannot be modified after creation).");
        pq3.setCourse(pythonCourse);

        Question pq4 = new Question();
        pq4.setText("What does the 'self' parameter refer to in a Python class method?");
        pq4.setOptions(Arrays.asList("The class itself", "The current instance of the class", "A global variable", "The parent class"));
        pq4.setCorrectOptionIndex(1);
        pq4.setExplanation("'self' refers to the current instance of the class, allowing access to its attributes and methods.");
        pq4.setCourse(pythonCourse);

        Question pq5 = new Question();
        pq5.setText("Which Python library is primarily used for numerical computing and arrays?");
        pq5.setOptions(Arrays.asList("Pandas", "NumPy", "Matplotlib", "Scikit-learn"));
        pq5.setCorrectOptionIndex(1);
        pq5.setExplanation("NumPy provides support for large multi-dimensional arrays and mathematical functions.");
        pq5.setCourse(pythonCourse);

        Question pq6 = new Question();
        pq6.setText("What is the correct way to create a dictionary in Python?");
        pq6.setOptions(Arrays.asList("d = [1, 2, 3]", "d = (1, 2, 3)", "d = {'key': 'value'}", "d = {1, 2, 3}"));
        pq6.setCorrectOptionIndex(2);
        pq6.setExplanation("Dictionaries use curly braces with key-value pairs separated by colons.");
        pq6.setCourse(pythonCourse);

        Question pq7 = new Question();
        pq7.setText("What does the 'lambda' keyword create in Python?");
        pq7.setOptions(Arrays.asList("A class", "A module", "An anonymous function", "A decorator"));
        pq7.setCorrectOptionIndex(2);
        pq7.setExplanation("Lambda creates small anonymous functions defined in a single expression.");
        pq7.setCourse(pythonCourse);

        Question pq8 = new Question();
        pq8.setText("Which method is used to add an element to the end of a list?");
        pq8.setOptions(Arrays.asList("add()", "insert()", "append()", "push()"));
        pq8.setCorrectOptionIndex(2);
        pq8.setExplanation("The append() method adds a single element to the end of a list.");
        pq8.setCourse(pythonCourse);

        Question pq9 = new Question();
        pq9.setText("What is the purpose of the '__init__' method in a Python class?");
        pq9.setOptions(Arrays.asList("To delete an object", "To initialize object attributes", "To import modules", "To define static methods"));
        pq9.setCorrectOptionIndex(1);
        pq9.setExplanation("__init__ is the constructor method called automatically when a new object is created.");
        pq9.setCourse(pythonCourse);

        Question pq10 = new Question();
        pq10.setText("What will 'print(2 ** 3)' output in Python?");
        pq10.setOptions(Arrays.asList("6", "8", "5", "23"));
        pq10.setCorrectOptionIndex(1);
        pq10.setExplanation("** is the exponentiation operator. 2 ** 3 means 2 raised to the power of 3 = 8.");
        pq10.setCourse(pythonCourse);

        questionRepository.saveAll(Arrays.asList(pq1, pq2, pq3, pq4, pq5, pq6, pq7, pq8, pq9, pq10));
        System.out.println("Python Quiz Questions seeded!");

        // 5. Seed Java Programming Masterclass
        Course javaCourse = new Course();
        javaCourse.setTitle("Java Programming Masterclass");
        javaCourse.setDescription("Master Java from scratch — covering fundamentals, OOP, collections, exception handling, threads, streams, and modern Java features with hands-on projects.");
        javaCourse.setImage("/assets/images/java-cover.png");
        javaCourse.setProgress(0);
        javaCourse.setModulesCount(7);

        List<Lesson> javaLessons = new ArrayList<>();

        // Module 1: Java Foundations
        javaLessons.add(createLesson("Java Introduction", "A beginner-friendly introduction to Java and why it remains one of the top programming languages.", "bm0OyhwFDuY", "20 min", "Java Foundations", javaCourse));
        javaLessons.add(createLesson("JDK Setup", "Step-by-step guide to installing the Java Development Kit on your system.", "WRISYpKhIrc", "15 min", "Java Foundations", javaCourse));
        javaLessons.add(createLesson("First Code in Java", "Write, compile, and run your very first Java program.", "tSqNBjGacYk", "18 min", "Java Foundations", javaCourse));
        javaLessons.add(createLesson("How Java Works", "Understand JVM, bytecode, and the compile-run cycle.", "NHrsLjhjmi4", "22 min", "Java Foundations", javaCourse));

        // Module 2: Variables, Data Types & Operators
        javaLessons.add(createLesson("Variables in Java", "Learn how to declare, initialize, and use variables.", "9RCuKrze_-k", "16 min", "Data Types & Operators", javaCourse));
        javaLessons.add(createLesson("Data Types in Java", "Explore primitive and reference data types in Java.", "Le25I331_yU", "20 min", "Data Types & Operators", javaCourse));
        javaLessons.add(createLesson("Type Conversion in Java", "Implicit and explicit type casting with examples.", "CPk8pffKV64", "18 min", "Data Types & Operators", javaCourse));
        javaLessons.add(createLesson("Arithmetic Operators", "Addition, subtraction, modulus, and more.", "flWjzwzgybI", "15 min", "Data Types & Operators", javaCourse));

        // Module 3: Control Flow
        javaLessons.add(createLesson("If Else in Java", "Conditional branching with if-else statements.", "74Q7POjS7mQ", "20 min", "Control Flow", javaCourse));
        javaLessons.add(createLesson("Switch Statement", "Multi-way branching using switch-case.", "IrQKDdptiw8", "18 min", "Control Flow", javaCourse));
        javaLessons.add(createLesson("While Loop in Java", "Repeated execution with while loops.", "mzt5tmV7wxI", "16 min", "Control Flow", javaCourse));
        javaLessons.add(createLesson("For Loop in Java", "Counter-based iteration with for loops.", "gu6Agiy2xBg", "18 min", "Control Flow", javaCourse));

        // Module 4: OOP Fundamentals
        javaLessons.add(createLesson("Class and Object Theory", "Understand the building blocks of OOP.", "Znmz_WxMxp4", "22 min", "OOP Fundamentals", javaCourse));
        javaLessons.add(createLesson("Methods in Java", "Define and call methods, understand return types and parameters.", "KSS3MUbBWLk", "20 min", "OOP Fundamentals", javaCourse));
        javaLessons.add(createLesson("Constructor in Java", "Default and parameterized constructors for object initialization.", "UC_aqNUFyVI", "18 min", "OOP Fundamentals", javaCourse));
        javaLessons.add(createLesson("Encapsulation", "Data hiding with access modifiers, getters, and setters.", "YbqneqDIZh8", "20 min", "OOP Fundamentals", javaCourse));

        // Module 5: Inheritance & Polymorphism
        javaLessons.add(createLesson("Inheritance in Java", "Code reuse through parent-child class relationships.", "dFuVh_Bzy9c", "22 min", "Inheritance & Polymorphism", javaCourse));
        javaLessons.add(createLesson("Polymorphism in Java", "Method overriding and dynamic dispatch.", "6U-0aUBiO5A", "20 min", "Inheritance & Polymorphism", javaCourse));
        javaLessons.add(createLesson("Abstract Keyword", "Abstract classes and methods for contract-based design.", "VJh2u7NLLDg", "18 min", "Inheritance & Polymorphism", javaCourse));
        javaLessons.add(createLesson("What is Interface", "Defining contracts with interfaces for loose coupling.", "A1uqgEz3hB0", "22 min", "Inheritance & Polymorphism", javaCourse));

        // Module 6: Exception Handling & Threads
        javaLessons.add(createLesson("What is Exception", "Understanding runtime errors and the exception hierarchy.", "5r_ERSm7NKE", "18 min", "Exceptions & Threads", javaCourse));
        javaLessons.add(createLesson("Try Catch Exception Handling", "Handling exceptions gracefully with try-catch-finally.", "osEjmECD8bI", "20 min", "Exceptions & Threads", javaCourse));
        javaLessons.add(createLesson("Threads in Java", "Introduction to multithreading and concurrency.", "KuvkahVyY9E", "25 min", "Exceptions & Threads", javaCourse));

        // Module 7: Collections & Streams
        javaLessons.add(createLesson("Collection API", "Overview of the Java Collections Framework.", "Kn1RbK02YpM", "22 min", "Collections & Streams", javaCourse));
        javaLessons.add(createLesson("ArrayList in Java", "Dynamic arrays with ArrayList and common operations.", "BqQ0qR8kmw8", "20 min", "Collections & Streams", javaCourse));
        javaLessons.add(createLesson("Lambda Expression", "Functional programming with lambda expressions.", "aecXHkZ-kJY", "22 min", "Collections & Streams", javaCourse));
        javaLessons.add(createLesson("Stream API", "Process collections with map, filter, and reduce.", "ak3BxYzSqsQ", "25 min", "Collections & Streams", javaCourse));

        javaCourse.setLessons(javaLessons);
        courseRepository.save(javaCourse);
        System.out.println("Java Programming Masterclass seeded with " + javaLessons.size() + " lessons!");

        // Java Quiz Questions (static fallback)
        Question jq1 = new Question();
        jq1.setText("What is the entry point of any Java application?");
        jq1.setOptions(Arrays.asList("start() method", "main() method", "init() method", "run() method"));
        jq1.setCorrectOptionIndex(1);
        jq1.setExplanation("The main() method with signature 'public static void main(String[] args)' is the entry point for any Java application.");
        jq1.setCourse(javaCourse);

        Question jq2 = new Question();
        jq2.setText("Which keyword is used to inherit a class in Java?");
        jq2.setOptions(Arrays.asList("implements", "inherits", "extends", "super"));
        jq2.setCorrectOptionIndex(2);
        jq2.setExplanation("The 'extends' keyword is used to create a subclass that inherits from a parent class.");
        jq2.setCourse(javaCourse);

        Question jq3 = new Question();
        jq3.setText("What is the default value of an int variable in Java?");
        jq3.setOptions(Arrays.asList("null", "0", "undefined", "-1"));
        jq3.setCorrectOptionIndex(1);
        jq3.setExplanation("Primitive numeric types like int default to 0 when declared as instance variables.");
        jq3.setCourse(javaCourse);

        Question jq4 = new Question();
        jq4.setText("Which collection does NOT allow duplicate elements?");
        jq4.setOptions(Arrays.asList("ArrayList", "LinkedList", "HashSet", "Vector"));
        jq4.setCorrectOptionIndex(2);
        jq4.setExplanation("HashSet implements the Set interface which does not allow duplicate elements.");
        jq4.setCourse(javaCourse);

        Question jq5 = new Question();
        jq5.setText("What is the purpose of the 'final' keyword when applied to a variable?");
        jq5.setOptions(Arrays.asList("Makes it static", "Makes it constant (cannot be reassigned)", "Makes it private", "Makes it volatile"));
        jq5.setCorrectOptionIndex(1);
        jq5.setExplanation("A final variable can only be assigned once and becomes a constant after initialization.");
        jq5.setCourse(javaCourse);

        Question jq6 = new Question();
        jq6.setText("What does JVM stand for?");
        jq6.setOptions(Arrays.asList("Java Virtual Machine", "Java Variable Manager", "Java Version Manager", "Java Visual Module"));
        jq6.setCorrectOptionIndex(0);
        jq6.setExplanation("JVM (Java Virtual Machine) is the runtime engine that executes Java bytecode, enabling platform independence.");
        jq6.setCourse(javaCourse);

        Question jq7 = new Question();
        jq7.setText("Which of the following is NOT a pillar of Object-Oriented Programming?");
        jq7.setOptions(Arrays.asList("Encapsulation", "Compilation", "Inheritance", "Polymorphism"));
        jq7.setCorrectOptionIndex(1);
        jq7.setExplanation("The four pillars of OOP are Encapsulation, Abstraction, Inheritance, and Polymorphism. Compilation is a process, not an OOP concept.");
        jq7.setCourse(javaCourse);

        Question jq8 = new Question();
        jq8.setText("What is the difference between '==' and '.equals()' in Java?");
        jq8.setOptions(Arrays.asList("They are identical", "'==' compares references, '.equals()' compares values", "'==' compares values, '.equals()' compares references", "'==' is for primitives only"));
        jq8.setCorrectOptionIndex(1);
        jq8.setExplanation("'==' checks if two references point to the same object in memory, while '.equals()' checks if the values/content of the objects are the same.");
        jq8.setCourse(javaCourse);

        Question jq9 = new Question();
        jq9.setText("Which exception is thrown when you try to access an array element with an invalid index?");
        jq9.setOptions(Arrays.asList("NullPointerException", "ArrayIndexOutOfBoundsException", "ClassCastException", "IllegalArgumentException"));
        jq9.setCorrectOptionIndex(1);
        jq9.setExplanation("ArrayIndexOutOfBoundsException is thrown when accessing an array with an index that is negative or >= the array's length.");
        jq9.setCourse(javaCourse);

        Question jq10 = new Question();
        jq10.setText("What does the Stream API's 'filter()' method do?");
        jq10.setOptions(Arrays.asList("Transforms each element", "Removes elements that don't match a predicate", "Sorts the stream", "Collects elements into a list"));
        jq10.setCorrectOptionIndex(1);
        jq10.setExplanation("filter() takes a Predicate and returns a stream containing only elements that match the given condition.");
        jq10.setCourse(javaCourse);

        questionRepository.saveAll(Arrays.asList(jq1, jq2, jq3, jq4, jq5, jq6, jq7, jq8, jq9, jq10));
        System.out.println("Java Quiz Questions seeded!");

        // 6. Data Structures & Algorithms Course
        Course dsaCourse = new Course();
        dsaCourse.setTitle("Data Structures & Algorithms Masterclass");
        dsaCourse.setDescription("Master DSA from scratch — arrays, linked lists, trees, graphs, sorting, searching, dynamic programming, and competitive coding strategies.");
        dsaCourse.setImage("https://images.unsplash.com/photo-1509228468518-180dd4864904?auto=format&fit=crop&q=80&w=800");
        dsaCourse.setProgress(0);
        dsaCourse.setModulesCount(6);

        List<Lesson> dsaLessons = new ArrayList<>();
        dsaLessons.add(createLesson("What are Data Structures?", "Introduction to data structures and why they matter.", "bum_19loj9A", "20 min", "DSA Foundations", dsaCourse));
        dsaLessons.add(createLesson("Big O Notation", "Time and space complexity analysis explained.", "v4cd1O4zkGw", "25 min", "DSA Foundations", dsaCourse));
        dsaLessons.add(createLesson("Arrays Explained", "Understanding arrays, operations, and memory layout.", "QJNwK2uJyGs", "22 min", "DSA Foundations", dsaCourse));
        dsaLessons.add(createLesson("Linked Lists", "Singly, doubly, and circular linked lists.", "njTh_OwMljA", "28 min", "DSA Foundations", dsaCourse));
        dsaLessons.add(createLesson("Stacks and Queues", "LIFO and FIFO data structures with real-world examples.", "wjI1WNcIntg", "22 min", "Linear Structures", dsaCourse));
        dsaLessons.add(createLesson("Hash Tables", "Hashing, collisions, and hash map implementations.", "shs0KM3wKv8", "25 min", "Linear Structures", dsaCourse));
        dsaLessons.add(createLesson("Recursion Fundamentals", "Base cases, call stack, and recursive thinking.", "IJDJ0kBx2LM", "20 min", "Linear Structures", dsaCourse));
        dsaLessons.add(createLesson("Binary Trees", "Tree terminology, traversals, and binary tree operations.", "fAAZixBzIAI", "28 min", "Trees & Graphs", dsaCourse));
        dsaLessons.add(createLesson("Binary Search Trees", "BST insertion, deletion, and search operations.", "cySVml6e_Fc", "25 min", "Trees & Graphs", dsaCourse));
        dsaLessons.add(createLesson("Heaps and Priority Queues", "Min-heap, max-heap, and heap sort.", "t0Cq6tVNRBA", "22 min", "Trees & Graphs", dsaCourse));
        dsaLessons.add(createLesson("Graph Theory Basics", "Vertices, edges, representations, BFS, and DFS.", "tWVWeAqZ0WU", "30 min", "Trees & Graphs", dsaCourse));
        dsaLessons.add(createLesson("Bubble Sort & Selection Sort", "Basic sorting algorithms with analysis.", "xli_FI7CuzA", "20 min", "Sorting & Searching", dsaCourse));
        dsaLessons.add(createLesson("Merge Sort", "Divide and conquer sorting with O(n log n) complexity.", "4VqmGXwpLqc", "25 min", "Sorting & Searching", dsaCourse));
        dsaLessons.add(createLesson("Quick Sort", "Partition-based sorting and pivot selection strategies.", "Hoixgm4-P4M", "22 min", "Sorting & Searching", dsaCourse));
        dsaLessons.add(createLesson("Binary Search", "Efficient searching in sorted arrays.", "P3YID7liBug", "18 min", "Sorting & Searching", dsaCourse));
        dsaLessons.add(createLesson("Dynamic Programming Intro", "Memoization, tabulation, and optimal substructure.", "oBt53YbR9Kk", "30 min", "Advanced DSA", dsaCourse));
        dsaLessons.add(createLesson("Dijkstra's Algorithm", "Shortest path algorithm for weighted graphs.", "pVfj6mxhdMw", "25 min", "Advanced DSA", dsaCourse));
        dsaLessons.add(createLesson("Greedy Algorithms", "Making locally optimal choices for global solutions.", "bC7o8P_Aste", "22 min", "Advanced DSA", dsaCourse));
        dsaLessons.add(createLesson("Sliding Window Technique", "Efficient subarray and substring problem solving.", "MK-NZ4hN7rs", "20 min", "Advanced DSA", dsaCourse));
        dsaLessons.add(createLesson("Top DSA Interview Problems", "Must-solve problems for coding interviews.", "KLlXCFG5TnA", "35 min", "Interview Prep", dsaCourse));

        dsaCourse.setLessons(dsaLessons);
        courseRepository.save(dsaCourse);
        System.out.println("DSA Masterclass seeded with " + dsaLessons.size() + " lessons!");

        // DSA Quiz Questions
        Question dq1 = new Question(); dq1.setText("What is the time complexity of binary search?"); dq1.setOptions(Arrays.asList("O(n)", "O(log n)", "O(n log n)", "O(1)")); dq1.setCorrectOptionIndex(1); dq1.setExplanation("Binary search halves the search space each iteration, giving O(log n) complexity."); dq1.setCourse(dsaCourse);
        Question dq2 = new Question(); dq2.setText("Which data structure uses LIFO ordering?"); dq2.setOptions(Arrays.asList("Queue", "Stack", "Array", "Linked List")); dq2.setCorrectOptionIndex(1); dq2.setExplanation("A Stack follows Last-In-First-Out (LIFO) ordering."); dq2.setCourse(dsaCourse);
        Question dq3 = new Question(); dq3.setText("What is the worst-case time complexity of Quick Sort?"); dq3.setOptions(Arrays.asList("O(n log n)", "O(n)", "O(n^2)", "O(log n)")); dq3.setCorrectOptionIndex(2); dq3.setExplanation("Quick Sort degrades to O(n^2) when the pivot is always the smallest or largest element."); dq3.setCourse(dsaCourse);
        Question dq4 = new Question(); dq4.setText("Which traversal visits the root node first in a binary tree?"); dq4.setOptions(Arrays.asList("Inorder", "Preorder", "Postorder", "Level Order")); dq4.setCorrectOptionIndex(1); dq4.setExplanation("Preorder traversal visits Root -> Left -> Right."); dq4.setCourse(dsaCourse);
        Question dq5 = new Question(); dq5.setText("What technique does Dynamic Programming use to avoid redundant computations?"); dq5.setOptions(Arrays.asList("Recursion", "Memoization", "Backtracking", "Brute Force")); dq5.setCorrectOptionIndex(1); dq5.setExplanation("Memoization stores previously computed results to avoid recalculating overlapping subproblems."); dq5.setCourse(dsaCourse);
        Question dq6 = new Question(); dq6.setText("Which data structure is best for implementing a LRU Cache?"); dq6.setOptions(Arrays.asList("Array", "Stack", "HashMap + Doubly Linked List", "Binary Tree")); dq6.setCorrectOptionIndex(2); dq6.setExplanation("A HashMap provides O(1) lookup while a Doubly Linked List maintains insertion order for eviction."); dq6.setCourse(dsaCourse);
        Question dq7 = new Question(); dq7.setText("What is the space complexity of Merge Sort?"); dq7.setOptions(Arrays.asList("O(1)", "O(log n)", "O(n)", "O(n^2)")); dq7.setCorrectOptionIndex(2); dq7.setExplanation("Merge Sort requires O(n) auxiliary space for the temporary arrays during merging."); dq7.setCourse(dsaCourse);
        Question dq8 = new Question(); dq8.setText("In a graph, what algorithm finds the shortest path from a single source?"); dq8.setOptions(Arrays.asList("DFS", "Kruskal's", "Dijkstra's", "Prim's")); dq8.setCorrectOptionIndex(2); dq8.setExplanation("Dijkstra's algorithm finds the shortest path from a single source to all other vertices in a weighted graph."); dq8.setCourse(dsaCourse);
        Question dq9 = new Question(); dq9.setText("What is a collision in a Hash Table?"); dq9.setOptions(Arrays.asList("When two keys map to the same index", "When the table is full", "When a key is deleted", "When the hash function fails")); dq9.setCorrectOptionIndex(0); dq9.setExplanation("A collision occurs when two different keys produce the same hash value and map to the same bucket."); dq9.setCourse(dsaCourse);
        Question dq10 = new Question(); dq10.setText("Which sorting algorithm is stable and has O(n log n) average complexity?"); dq10.setOptions(Arrays.asList("Quick Sort", "Heap Sort", "Merge Sort", "Selection Sort")); dq10.setCorrectOptionIndex(2); dq10.setExplanation("Merge Sort is stable and guarantees O(n log n) in all cases."); dq10.setCourse(dsaCourse);
        questionRepository.saveAll(Arrays.asList(dq1, dq2, dq3, dq4, dq5, dq6, dq7, dq8, dq9, dq10));
        System.out.println("DSA Quiz Questions seeded!");

        // 7. DevOps & Cloud Computing Course
        Course devopsCourse = new Course();
        devopsCourse.setTitle("DevOps & Cloud Computing Essentials");
        devopsCourse.setDescription("Learn Docker, Kubernetes, CI/CD pipelines, AWS, Linux, and modern DevOps practices for production-grade deployments.");
        devopsCourse.setImage("https://images.unsplash.com/photo-1667372393119-3d4c48d07fc9?auto=format&fit=crop&q=80&w=800");
        devopsCourse.setProgress(0);
        devopsCourse.setModulesCount(5);

        List<Lesson> devopsLessons = new ArrayList<>();
        devopsLessons.add(createLesson("What is DevOps?", "Understanding DevOps culture, practices, and tools.", "Xrgk023l4lI", "20 min", "DevOps Foundations", devopsCourse));
        devopsLessons.add(createLesson("Linux Basics for DevOps", "Essential Linux commands every DevOps engineer needs.", "ROjZy1WbCIA", "30 min", "DevOps Foundations", devopsCourse));
        devopsLessons.add(createLesson("Git & GitHub for Teams", "Version control workflows, branching strategies, and PR reviews.", "RGOj5yH7evk", "25 min", "DevOps Foundations", devopsCourse));
        devopsLessons.add(createLesson("Networking Basics", "TCP/IP, DNS, load balancers, and networking fundamentals.", "IPvYjXCsTg8", "22 min", "DevOps Foundations", devopsCourse));
        devopsLessons.add(createLesson("Docker Tutorial for Beginners", "Containers, images, Dockerfiles, and Docker Compose.", "pTFZFxd4hOI", "35 min", "Containers", devopsCourse));
        devopsLessons.add(createLesson("Docker Compose Deep Dive", "Multi-container applications with Docker Compose.", "HG6yIjZapSA", "25 min", "Containers", devopsCourse));
        devopsLessons.add(createLesson("Kubernetes Explained", "Container orchestration, pods, services, and deployments.", "X48VuDVv0do", "30 min", "Containers", devopsCourse));
        devopsLessons.add(createLesson("Kubernetes Hands-On", "Deploy your first app on a Kubernetes cluster.", "s_o8dwzRlu4", "35 min", "Containers", devopsCourse));
        devopsLessons.add(createLesson("CI/CD Pipeline with Jenkins", "Automate build, test, and deploy with Jenkins.", "7KCS70sCoK0", "28 min", "CI/CD Pipelines", devopsCourse));
        devopsLessons.add(createLesson("GitHub Actions Tutorial", "Build CI/CD workflows directly in GitHub.", "R8_veQiYBjI", "25 min", "CI/CD Pipelines", devopsCourse));
        devopsLessons.add(createLesson("Terraform Infrastructure as Code", "Provision cloud resources with Terraform.", "SLB_c_ayRMo", "30 min", "CI/CD Pipelines", devopsCourse));
        devopsLessons.add(createLesson("AWS for Beginners", "EC2, S3, RDS, and core AWS services overview.", "k1RI5locZE4", "35 min", "Cloud Platforms", devopsCourse));
        devopsLessons.add(createLesson("AWS Lambda & Serverless", "Build serverless applications with AWS Lambda.", "eOBq__h4OJ4", "25 min", "Cloud Platforms", devopsCourse));
        devopsLessons.add(createLesson("Monitoring with Prometheus & Grafana", "Application and infrastructure monitoring.", "9TJx7QTrGuQ", "28 min", "Cloud Platforms", devopsCourse));
        devopsLessons.add(createLesson("Ansible Automation", "Configuration management and automation with Ansible.", "1id6ERvfozo", "25 min", "Cloud Platforms", devopsCourse));
        devopsLessons.add(createLesson("DevOps Interview Prep", "Top DevOps interview questions and scenarios.", "clnVYr18mFo", "30 min", "Career & Interview", devopsCourse));

        devopsCourse.setLessons(devopsLessons);
        courseRepository.save(devopsCourse);
        System.out.println("DevOps & Cloud course seeded with " + devopsLessons.size() + " lessons!");

        // DevOps Quiz Questions
        Question dvq1 = new Question(); dvq1.setText("What is a Docker container?"); dvq1.setOptions(Arrays.asList("A virtual machine", "A lightweight isolated process using OS-level virtualization", "A cloud server", "A database instance")); dvq1.setCorrectOptionIndex(1); dvq1.setExplanation("Containers share the host OS kernel and isolate applications using namespaces and cgroups."); dvq1.setCourse(devopsCourse);
        Question dvq2 = new Question(); dvq2.setText("What does CI/CD stand for?"); dvq2.setOptions(Arrays.asList("Code Integration / Code Deployment", "Continuous Integration / Continuous Delivery", "Container Integration / Container Deployment", "Cloud Integration / Cloud Delivery")); dvq2.setCorrectOptionIndex(1); dvq2.setExplanation("CI/CD automates the process of integrating code changes and delivering them to production."); dvq2.setCourse(devopsCourse);
        Question dvq3 = new Question(); dvq3.setText("What is a Kubernetes Pod?"); dvq3.setOptions(Arrays.asList("A cluster of VMs", "The smallest deployable unit containing one or more containers", "A network policy", "A storage volume")); dvq3.setCorrectOptionIndex(1); dvq3.setExplanation("A Pod is the smallest unit in Kubernetes, encapsulating one or more tightly coupled containers."); dvq3.setCourse(devopsCourse);
        Question dvq4 = new Question(); dvq4.setText("Which file defines a Docker image's build steps?"); dvq4.setOptions(Arrays.asList("docker-compose.yml", "Dockerfile", "Makefile", "package.json")); dvq4.setCorrectOptionIndex(1); dvq4.setExplanation("A Dockerfile contains instructions to build a Docker image layer by layer."); dvq4.setCourse(devopsCourse);
        Question dvq5 = new Question(); dvq5.setText("What does Terraform primarily manage?"); dvq5.setOptions(Arrays.asList("Application code", "Infrastructure as Code", "Database queries", "User authentication")); dvq5.setCorrectOptionIndex(1); dvq5.setExplanation("Terraform provisions and manages cloud infrastructure declaratively using HCL configuration files."); dvq5.setCourse(devopsCourse);
        Question dvq6 = new Question(); dvq6.setText("What AWS service provides scalable object storage?"); dvq6.setOptions(Arrays.asList("EC2", "RDS", "S3", "Lambda")); dvq6.setCorrectOptionIndex(2); dvq6.setExplanation("Amazon S3 (Simple Storage Service) provides scalable, durable object storage."); dvq6.setCourse(devopsCourse);
        Question dvq7 = new Question(); dvq7.setText("What is the purpose of a load balancer?"); dvq7.setOptions(Arrays.asList("Encrypt data", "Distribute traffic across multiple servers", "Store backups", "Monitor logs")); dvq7.setCorrectOptionIndex(1); dvq7.setExplanation("A load balancer distributes incoming traffic across multiple servers to ensure availability and reliability."); dvq7.setCourse(devopsCourse);
        Question dvq8 = new Question(); dvq8.setText("Which command lists all running Docker containers?"); dvq8.setOptions(Arrays.asList("docker images", "docker ps", "docker run", "docker build")); dvq8.setCorrectOptionIndex(1); dvq8.setExplanation("'docker ps' lists all currently running containers. Add '-a' to include stopped ones."); dvq8.setCourse(devopsCourse);
        Question dvq9 = new Question(); dvq9.setText("What is Ansible primarily used for?"); dvq9.setOptions(Arrays.asList("Container orchestration", "Configuration management and automation", "Source control", "Database management")); dvq9.setCorrectOptionIndex(1); dvq9.setExplanation("Ansible automates configuration management, application deployment, and task automation using YAML playbooks."); dvq9.setCourse(devopsCourse);
        Question dvq10 = new Question(); dvq10.setText("What is a microservices architecture?"); dvq10.setOptions(Arrays.asList("A monolithic application design", "An architecture where an app is built as small independent services", "A database sharding strategy", "A frontend framework")); dvq10.setCorrectOptionIndex(1); dvq10.setExplanation("Microservices decompose an application into small, loosely coupled services that can be developed and deployed independently."); dvq10.setCourse(devopsCourse);
        questionRepository.saveAll(Arrays.asList(dvq1, dvq2, dvq3, dvq4, dvq5, dvq6, dvq7, dvq8, dvq9, dvq10));
        System.out.println("DevOps Quiz Questions seeded!");

        // 8. React & Next.js Course
        Course reactCourse = new Course();
        reactCourse.setTitle("React & Next.js Full Stack Development");
        reactCourse.setDescription("Build modern web applications with React 19, Next.js 15, hooks, server components, API routes, authentication, and full-stack deployment.");
        reactCourse.setImage("https://images.unsplash.com/photo-1633356122102-3fe601e05bd2?auto=format&fit=crop&q=80&w=800");
        reactCourse.setProgress(0);
        reactCourse.setModulesCount(5);

        List<Lesson> reactLessons = new ArrayList<>();
        reactLessons.add(createLesson("React JS Tutorial for Beginners", "Introduction to React, JSX, and component-based architecture.", "SqcY0GlETPk", "30 min", "React Fundamentals", reactCourse));
        reactLessons.add(createLesson("React Components & Props", "Building reusable UI components with props.", "Cla1WwguArA", "25 min", "React Fundamentals", reactCourse));
        reactLessons.add(createLesson("useState Hook", "Managing component state with the useState hook.", "O6P86uwfdR0", "20 min", "React Fundamentals", reactCourse));
        reactLessons.add(createLesson("useEffect Hook", "Side effects, data fetching, and cleanup.", "0ZJgIjIuY7U", "22 min", "React Fundamentals", reactCourse));
        reactLessons.add(createLesson("React Router v6", "Client-side routing with React Router.", "Ul3y1LXxzdU", "25 min", "State & Routing", reactCourse));
        reactLessons.add(createLesson("Context API & useContext", "Global state management without external libraries.", "5LrDIWkK_Bc", "22 min", "State & Routing", reactCourse));
        reactLessons.add(createLesson("Redux Toolkit Tutorial", "Modern state management with Redux Toolkit.", "bbkBuqC1rU4", "30 min", "State & Routing", reactCourse));
        reactLessons.add(createLesson("React Forms & Validation", "Controlled components, form handling, and validation.", "SdzMBWT2CDQ", "25 min", "State & Routing", reactCourse));
        reactLessons.add(createLesson("Next.js 14 Full Course", "Server components, app router, and full-stack Next.js.", "wm5gMKuwSYk", "35 min", "Next.js", reactCourse));
        reactLessons.add(createLesson("Next.js API Routes", "Build backend APIs within your Next.js application.", "mTz0GXj8NN0", "22 min", "Next.js", reactCourse));
        reactLessons.add(createLesson("Next.js Authentication", "Implement auth with NextAuth.js and JWT.", "w2h54xz6Ndw", "28 min", "Next.js", reactCourse));
        reactLessons.add(createLesson("Server vs Client Components", "Understanding the React Server Components paradigm.", "TQQPAU21ZUw", "20 min", "Next.js", reactCourse));
        reactLessons.add(createLesson("React Custom Hooks", "Create reusable logic with custom hooks.", "6ThXsUwLWvc", "22 min", "Advanced React", reactCourse));
        reactLessons.add(createLesson("React Performance Optimization", "useMemo, useCallback, React.memo, and lazy loading.", "lGEMwh32soc", "25 min", "Advanced React", reactCourse));
        reactLessons.add(createLesson("Tailwind CSS with React", "Utility-first styling for React components.", "dFgzHOX84xQ", "22 min", "Advanced React", reactCourse));
        reactLessons.add(createLesson("Full Stack React Project", "Build and deploy a complete full-stack application.", "CvCiNeLnZ00", "40 min", "Deployment", reactCourse));

        reactCourse.setLessons(reactLessons);
        courseRepository.save(reactCourse);
        System.out.println("React & Next.js course seeded with " + reactLessons.size() + " lessons!");

        // React Quiz Questions
        Question rq1 = new Question(); rq1.setText("What is JSX in React?"); rq1.setOptions(Arrays.asList("A database query language", "A syntax extension that allows HTML in JavaScript", "A CSS framework", "A testing library")); rq1.setCorrectOptionIndex(1); rq1.setExplanation("JSX is a syntax extension for JavaScript that lets you write HTML-like markup inside JavaScript files."); rq1.setCourse(reactCourse);
        Question rq2 = new Question(); rq2.setText("What hook is used to manage state in functional components?"); rq2.setOptions(Arrays.asList("useEffect", "useState", "useRef", "useMemo")); rq2.setCorrectOptionIndex(1); rq2.setExplanation("useState returns a state variable and a setter function for managing component state."); rq2.setCourse(reactCourse);
        Question rq3 = new Question(); rq3.setText("What is the Virtual DOM?"); rq3.setOptions(Arrays.asList("The actual browser DOM", "A lightweight copy of the real DOM for efficient updates", "A server-side rendering engine", "A CSS optimization tool")); rq3.setCorrectOptionIndex(1); rq3.setExplanation("React maintains a Virtual DOM to batch and minimize actual DOM manipulations for better performance."); rq3.setCourse(reactCourse);
        Question rq4 = new Question(); rq4.setText("What does useEffect's empty dependency array [] mean?"); rq4.setOptions(Arrays.asList("Effect runs on every render", "Effect runs only once on mount", "Effect never runs", "Effect runs when state changes")); rq4.setCorrectOptionIndex(1); rq4.setExplanation("An empty dependency array tells React to run the effect only once after the initial render."); rq4.setCourse(reactCourse);
        Question rq5 = new Question(); rq5.setText("What is Next.js primarily known for?"); rq5.setOptions(Arrays.asList("Mobile app development", "Server-side rendering and full-stack React framework", "Database management", "CSS preprocessing")); rq5.setCorrectOptionIndex(1); rq5.setExplanation("Next.js is a React framework that provides SSR, SSG, API routes, and full-stack capabilities."); rq5.setCourse(reactCourse);
        Question rq6 = new Question(); rq6.setText("What is prop drilling in React?"); rq6.setOptions(Arrays.asList("A testing technique", "Passing props through many nested components", "A performance optimization", "A routing method")); rq6.setCorrectOptionIndex(1); rq6.setExplanation("Prop drilling is passing data through multiple component layers, which Context API or state managers solve."); rq6.setCourse(reactCourse);
        Question rq7 = new Question(); rq7.setText("What is the purpose of React.memo()?"); rq7.setOptions(Arrays.asList("To create memos in the app", "To prevent unnecessary re-renders of a component", "To store data in memory", "To create side effects")); rq7.setCorrectOptionIndex(1); rq7.setExplanation("React.memo is a higher-order component that skips re-rendering if props haven't changed."); rq7.setCourse(reactCourse);
        Question rq8 = new Question(); rq8.setText("In Next.js App Router, what is a Server Component?"); rq8.setOptions(Arrays.asList("A component that only runs in the browser", "A component that renders on the server without sending JS to the client", "A REST API endpoint", "A database model")); rq8.setCorrectOptionIndex(1); rq8.setExplanation("Server Components render on the server and send only HTML to the client, reducing bundle size."); rq8.setCourse(reactCourse);
        Question rq9 = new Question(); rq9.setText("What does the 'key' prop help React with?"); rq9.setOptions(Arrays.asList("Styling elements", "Identifying which list items have changed for efficient re-rendering", "Authentication", "Routing")); rq9.setCorrectOptionIndex(1); rq9.setExplanation("Keys help React identify which items in a list have changed, been added, or removed for efficient DOM updates."); rq9.setCourse(reactCourse);
        Question rq10 = new Question(); rq10.setText("What is Redux used for?"); rq10.setOptions(Arrays.asList("Server-side rendering", "Predictable global state management", "CSS styling", "Database queries")); rq10.setCorrectOptionIndex(1); rq10.setExplanation("Redux provides a centralized store for managing application state with predictable state transitions via actions and reducers."); rq10.setCourse(reactCourse);
        questionRepository.saveAll(Arrays.asList(rq1, rq2, rq3, rq4, rq5, rq6, rq7, rq8, rq9, rq10));
        System.out.println("React Quiz Questions seeded!");

        // 9. Cybersecurity Essentials Course
        Course cyberCourse = new Course();
        cyberCourse.setTitle("Cybersecurity Essentials");
        cyberCourse.setDescription("Learn the core principles of digital security: CIA triad, network defense, access control, and incident response.");
        cyberCourse.setImage("https://images.unsplash.com/photo-1550751827-4bd374c3f58b?auto=format&fit=crop&q=80&w=800");
        cyberCourse.setProgress(0);
        cyberCourse.setModulesCount(4);

        List<Lesson> cyberLessons = new ArrayList<>();
        cyberLessons.add(createLesson("What is Cybersecurity?", "Introduction to cybersecurity concepts and career paths.", "inWWhr5tnEA", "20 min", "Foundations", cyberCourse));
        cyberLessons.add(createLesson("Types of Cyber Attacks", "Phishing, malware, ransomware, DDoS, and social engineering.", "Dk-ZqQ-bfy4", "25 min", "Foundations", cyberCourse));
        cyberLessons.add(createLesson("CIA Triad Explained", "Confidentiality, Integrity, and Availability fundamentals.", "6vri_YDyGek", "18 min", "Foundations", cyberCourse));
        cyberLessons.add(createLesson("Networking for Security", "TCP/IP, firewalls, VPNs, and network defense basics.", "qiQR5rTSshw", "28 min", "Network Defense", cyberCourse));
        cyberLessons.add(createLesson("Cryptography Basics", "Symmetric, asymmetric encryption, hashing, and digital signatures.", "6_Cxj5WKpIw", "25 min", "Foundations", cyberCourse));
        cyberLessons.add(createLesson("SSL/TLS and HTTPS", "How SSL certificates and HTTPS secure web traffic.", "j9QmMEWmcfo", "20 min", "Foundations", cyberCourse));
        cyberLessons.add(createLesson("Incident Response", "Handling security breaches and incident management.", "FvUMnOa1s14", "25 min", "Operations", cyberCourse));
        cyberLessons.add(createLesson("SOC Analyst Skills", "Security Operations Center tools and workflows.", "gSp7I7y7Y4I", "30 min", "Career", cyberCourse));

        cyberCourse.setLessons(cyberLessons);
        courseRepository.save(cyberCourse);

        Question cq1 = new Question(); cq1.setText("What does the CIA triad stand for?"); cq1.setOptions(Arrays.asList("Central Intelligence Agency", "Confidentiality, Integrity, Availability", "Control, Identity, Access", "Cyber Intelligence Analysis")); cq1.setCorrectOptionIndex(1); cq1.setExplanation("CIA triad covers Confidentiality, Integrity, and Availability."); cq1.setCourse(cyberCourse);
        Question cq2 = new Question(); cq2.setText("What is Phishing?"); cq2.setOptions(Arrays.asList("DDoS", "Deceptive emails to steal data", "Brute Force", "SQL Injection")); cq2.setCorrectOptionIndex(1); cq2.setExplanation("Phishing uses fake emails to trick users."); cq2.setCourse(cyberCourse);
        questionRepository.saveAll(Arrays.asList(cq1, cq2));

        // 10. Ethical Hacking & Pentesting Course
        Course hackCourse = new Course();
        hackCourse.setTitle("Ethical Hacking & Penetration Testing");
        hackCourse.setDescription("Master the art of ethical hacking. Learn reconnaissance, scanning, vulnerability analysis, and exploitation techniques.");
        hackCourse.setImage("https://images.unsplash.com/photo-1563986768609-322da13575f3?auto=format&fit=crop&q=80&w=800");
        hackCourse.setProgress(0);
        hackCourse.setModulesCount(5);

        List<Lesson> hackLessons = new ArrayList<>();
        hackLessons.add(createLesson("Ethical Hacking Introduction", "What is ethical hacking and penetration testing phases.", "3Kq1MIfTWCE", "22 min", "Introduction", hackCourse));
        hackLessons.add(createLesson("Kali Linux Setup", "Installing and configuring Kali Linux for pen testing.", "lZAoFs75_cs", "25 min", "Environment Setup", hackCourse));
        hackLessons.add(createLesson("Nmap Network Scanning", "Port scanning and network reconnaissance with Nmap.", "4t4kBkMsDbQ", "22 min", "Reconnaissance", hackCourse));
        hackLessons.add(createLesson("Web Application Security", "OWASP Top 10, SQL injection, XSS, and CSRF attacks.", "WtHnT73NaaQ", "30 min", "Web Hacking", hackCourse));
        hackLessons.add(createLesson("Password Cracking Techniques", "Brute force, dictionary attacks, and password security.", "7U-RbOKanYs", "22 min", "Exploitation", hackCourse));
        hackLessons.add(createLesson("Wireshark Packet Analysis", "Capturing and analyzing network traffic.", "lb1Dw0elw0Q", "28 min", "Reconnaissance", hackCourse));
        hackLessons.add(createLesson("Metasploit Framework", "Automating exploitation with Metasploit.", "i_fK2X7u80k", "30 min", "Exploitation", hackCourse));
        hackLessons.add(createLesson("Burp Suite Basics", "Intercepting proxies for web app testing.", "m-SIn_6S_4w", "25 min", "Web Hacking", hackCourse));

        hackCourse.setLessons(hackLessons);
        courseRepository.save(hackCourse);

        Question hq1 = new Question(); hq1.setText("Which tool is used for port scanning?"); hq1.setOptions(Arrays.asList("Wireshark", "Nmap", "Metasploit", "Burp Suite")); hq1.setCorrectOptionIndex(1); hq1.setExplanation("Nmap is the industry standard for port scanning."); hq1.setCourse(hackCourse);
        Question hq2 = new Question(); hq2.setText("What is the primary purpose of Burp Suite?"); hq2.setOptions(Arrays.asList("Scanning network ports", "Intercepting and testing web requests", "Cracking passwords", "Analyzing wifi signals")); hq2.setCorrectOptionIndex(1); hq2.setExplanation("Burp Suite is used for web application security testing."); hq2.setCourse(hackCourse);
        questionRepository.saveAll(Arrays.asList(hq1, hq2));
        System.out.println("Ethical Hacking course seeded!");

        // 10. Spring Boot & Microservices Course
        Course springCourse = new Course();
        springCourse.setTitle("Spring Boot & Microservices Architecture");
        springCourse.setDescription("Build enterprise-grade Java applications with Spring Boot, REST APIs, Spring Security, JPA, microservices patterns, and cloud deployment.");
        springCourse.setImage("https://images.unsplash.com/photo-1605379399642-870262d3d051?auto=format&fit=crop&q=80&w=800");
        springCourse.setProgress(0);
        springCourse.setModulesCount(5);

        List<Lesson> springLessons = new ArrayList<>();
        springLessons.add(createLesson("What is Spring Boot?", "Introduction to Spring Boot and why it simplifies Java development.", "9SGDpanrc8U", "20 min", "Spring Boot Basics", springCourse));
        springLessons.add(createLesson("Spring Boot Project Setup", "Creating your first Spring Boot project with Spring Initializr.", "HYGnVeCs0Yg", "22 min", "Spring Boot Basics", springCourse));
        springLessons.add(createLesson("Spring Boot REST API", "Building RESTful web services with @RestController.", "bKjyLMSjvjY", "25 min", "Spring Boot Basics", springCourse));
        springLessons.add(createLesson("Spring Boot Annotations", "Essential annotations: @Autowired, @Component, @Service, @Repository.", "K43qbRsi50s", "20 min", "Spring Boot Basics", springCourse));
        springLessons.add(createLesson("Spring Data JPA & Hibernate", "Database operations with JPA repositories and entity mapping.", "8SGI_XS5OPw", "28 min", "Data Layer", springCourse));
        springLessons.add(createLesson("MySQL with Spring Boot", "Connecting Spring Boot to MySQL databases.", "Kn1RbK02YpM", "25 min", "Data Layer", springCourse));
        springLessons.add(createLesson("Spring Boot Validation", "Request validation with Bean Validation and custom validators.", "gPnd-hzM_6A", "20 min", "Data Layer", springCourse));
        springLessons.add(createLesson("Exception Handling in Spring", "Global error handling with @ControllerAdvice.", "PzK4ZXa2Tbc", "22 min", "Data Layer", springCourse));
        springLessons.add(createLesson("Spring Security Basics", "Authentication and authorization with Spring Security.", "her_7pa0vrg", "30 min", "Security & Auth", springCourse));
        springLessons.add(createLesson("JWT Authentication", "Implementing token-based auth with JSON Web Tokens.", "UaB-0e76LdQ", "28 min", "Security & Auth", springCourse));
        springLessons.add(createLesson("Role-Based Access Control", "Securing endpoints based on user roles.", "IyzC1kkHvT8", "22 min", "Security & Auth", springCourse));
        springLessons.add(createLesson("Microservices Architecture", "Designing scalable systems with microservices patterns.", "lTAcCNbJ7KE", "30 min", "Microservices", springCourse));
        springLessons.add(createLesson("Spring Cloud Gateway", "API Gateway for routing and load balancing microservices.", "1vjOv_f9L8I", "25 min", "Microservices", springCourse));
        springLessons.add(createLesson("Service Discovery with Eureka", "Dynamic service registration and discovery.", "y8IQb4ofjDo", "22 min", "Microservices", springCourse));
        springLessons.add(createLesson("Docker with Spring Boot", "Containerizing Spring Boot applications.", "DgVjkKrGyGs", "25 min", "Deployment", springCourse));
        springLessons.add(createLesson("Deploying to AWS", "Deploying Spring Boot apps on AWS EC2 and Elastic Beanstalk.", "S4JD_gSFLPs", "30 min", "Deployment", springCourse));

        springCourse.setLessons(springLessons);
        courseRepository.save(springCourse);
        System.out.println("Spring Boot course seeded with " + springLessons.size() + " lessons!");

        Question sbq1 = new Question(); sbq1.setText("What annotation marks a class as a Spring Boot application entry point?"); sbq1.setOptions(Arrays.asList("@Component", "@SpringBootApplication", "@RestController", "@Service")); sbq1.setCorrectOptionIndex(1); sbq1.setExplanation("@SpringBootApplication combines @Configuration, @EnableAutoConfiguration, and @ComponentScan."); sbq1.setCourse(springCourse);
        Question sbq2 = new Question(); sbq2.setText("What does @Autowired do in Spring?"); sbq2.setOptions(Arrays.asList("Creates a new thread", "Automatically injects a dependency", "Defines a REST endpoint", "Encrypts data")); sbq2.setCorrectOptionIndex(1); sbq2.setExplanation("@Autowired tells Spring to automatically inject the required bean dependency."); sbq2.setCourse(springCourse);
        Question sbq3 = new Question(); sbq3.setText("Which annotation maps an HTTP GET request to a method?"); sbq3.setOptions(Arrays.asList("@PostMapping", "@GetMapping", "@PutMapping", "@RequestBody")); sbq3.setCorrectOptionIndex(1); sbq3.setExplanation("@GetMapping is a shortcut for @RequestMapping(method = RequestMethod.GET)."); sbq3.setCourse(springCourse);
        Question sbq4 = new Question(); sbq4.setText("What is JPA in Spring Boot?"); sbq4.setOptions(Arrays.asList("A frontend framework", "Java Persistence API for ORM database access", "A testing library", "A build tool")); sbq4.setCorrectOptionIndex(1); sbq4.setExplanation("JPA provides a specification for ORM, mapping Java objects to database tables."); sbq4.setCourse(springCourse);
        Question sbq5 = new Question(); sbq5.setText("What is the purpose of application.properties?"); sbq5.setOptions(Arrays.asList("Defining HTML templates", "Configuring app settings like DB connection and server port", "Writing test cases", "Managing dependencies")); sbq5.setCorrectOptionIndex(1); sbq5.setExplanation("application.properties configures Spring Boot settings like database URL, server port, and more."); sbq5.setCourse(springCourse);
        Question sbq6 = new Question(); sbq6.setText("What does @RestController combine?"); sbq6.setOptions(Arrays.asList("@Service + @Repository", "@Controller + @ResponseBody", "@Component + @Bean", "@Entity + @Table")); sbq6.setCorrectOptionIndex(1); sbq6.setExplanation("@RestController = @Controller + @ResponseBody, returning data directly as JSON."); sbq6.setCourse(springCourse);
        Question sbq7 = new Question(); sbq7.setText("What is a microservice?"); sbq7.setOptions(Arrays.asList("A very small program", "An independently deployable service focused on a single business capability", "A type of database", "A frontend component")); sbq7.setCorrectOptionIndex(1); sbq7.setExplanation("Microservices are small, autonomous services that work together and can be deployed independently."); sbq7.setCourse(springCourse);
        Question sbq8 = new Question(); sbq8.setText("What does Spring Security provide?"); sbq8.setOptions(Arrays.asList("Database migrations", "Authentication, authorization, and protection against attacks", "Frontend routing", "Logging")); sbq8.setCorrectOptionIndex(1); sbq8.setExplanation("Spring Security handles authentication, authorization, CSRF protection, and more."); sbq8.setCourse(springCourse);
        Question sbq9 = new Question(); sbq9.setText("What is Eureka in microservices?"); sbq9.setOptions(Arrays.asList("A database", "A service discovery server", "A message queue", "A load balancer")); sbq9.setCorrectOptionIndex(1); sbq9.setExplanation("Netflix Eureka is a service registry that allows microservices to find and communicate with each other."); sbq9.setCourse(springCourse);
        Question sbq10 = new Question(); sbq10.setText("What is the default embedded server in Spring Boot?"); sbq10.setOptions(Arrays.asList("Nginx", "Apache", "Tomcat", "Jetty")); sbq10.setCorrectOptionIndex(2); sbq10.setExplanation("Spring Boot uses embedded Tomcat as its default web server."); sbq10.setCourse(springCourse);
        questionRepository.saveAll(Arrays.asList(sbq1, sbq2, sbq3, sbq4, sbq5, sbq6, sbq7, sbq8, sbq9, sbq10));
        System.out.println("Spring Boot Quiz Questions seeded!");

        System.out.println("Database fully seeded with all courses!");
    }

    private Lesson createLesson(String title, String description, String videoUrl, String duration, String module, Course course) {
        Lesson lesson = new Lesson();
        lesson.setTitle(title);
        lesson.setDescription(description);
        lesson.setVideoUrl(videoUrl);
        lesson.setDuration(duration);
        lesson.setModule(module);
        lesson.setCourse(course);
        return lesson;
    }
}
