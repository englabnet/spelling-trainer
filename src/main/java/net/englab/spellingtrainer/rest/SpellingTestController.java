package net.englab.spellingtrainer.rest;

import lombok.RequiredArgsConstructor;
import net.englab.spellingtrainer.models.TestStep;
import net.englab.spellingtrainer.models.entities.SpellingTest;
import net.englab.spellingtrainer.services.SpellingTestService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * A REST controller that handles creating and passing spelling tests.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tests")
public class SpellingTestController {

    private final SpellingTestService testService;

    /**
     * Generates a new spelling test with the given list of words.
     *
     * @param wordIds a list of word IDs
     * @return an ID of the new test
     */
    @PostMapping
    public String generate(@RequestBody List<Integer> wordIds) {
        return testService.generate(wordIds);
    }

    /**
     * Finds a spelling test by its ID.
     *
     * @param testId the ID of the test
     * @return the test data
     */
    @GetMapping("/{testId}")
    public SpellingTest get(@PathVariable String testId) {
        return testService.find(testId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The test has not been found"));
    }

    @GetMapping("/{testId}/{step}")
    public TestStep getStep(@PathVariable String testId, @PathVariable int step) {
        return testService.findStep(testId, step)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The step has not been found"));
    }

    @GetMapping("/{testId}/{step}/check")
    public boolean checkAnswer(@PathVariable String testId, @PathVariable int step, String answer) {
        return testService.checkAnswer(testId, step, answer);
    }
}
