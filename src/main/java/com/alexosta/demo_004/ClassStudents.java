package com.alexosta.demo_004;

public class ClassStudents implements Comparable<ClassStudents>{
    private String name;
//    private String example;

    public ClassStudents(String name) {
        this.name = name;
    }

//    public String getExample() {
//        return example;
//    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(ClassStudents other) {
        return this.name.compareTo(other.name);
    }
}
